const User = require("../models/User")
const jwt = require("jsonwebtoken")
const bcrypt = require("bcryptjs")
const fetch = require("node-fetch")

const generateAccessToken = (user) => {
  return jwt.sign(
    { id: user._id, role: user.role },
    process.env.JWT_SECRET,
    { expiresIn: process.env.JWT_EXPIRES_IN || "15m" }
  )
}

const generateRefreshToken = (user) => {
  return jwt.sign(
    { id: user._id },
    process.env.JWT_REFRESH_SECRET,
    { expiresIn: process.env.JWT_REFRESH_EXPIRES_IN || "7d" }
  )
}

const geocodeCity = async (city) => {
  const response = await fetch(
    `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(city)}&format=json&limit=1`,
    {
      headers: {
        "User-Agent": "Doctor-App/1.0 (contact: test@example.com)",
        "Accept": "application/json"
      }
    }
  )

  if (!response.ok) {
    throw new Error("Failed to fetch location data")
  }

  const data = await response.json()

  if (!data.length) {
    throw new Error("Invalid city name")
  }

  return {
    lat: Number(data[0].lat),
    lng: Number(data[0].lon)
  }
}


exports.register = async (req, res) => {
  try {
    const {
      role,
      name,
      email,
      mobileNumber,
      password,
      gender,
      dob,
      fee,
      speciality,
      city,
      address
    } = req.body

    if (!role || !name || !email || !mobileNumber || !password) {
      return res.status(400).json({
        success: false,
        message: "Required fields missing"
      })
    }

    if (role === "DOCTOR" && (!fee || !speciality || !city)) {
      return res.status(400).json({
        success: false,
        message: "Doctor must provide fee, speciality and location"
      })
    }

    const existingUser = await User.findOne({ mobileNumber })
    if (existingUser) {
      return res.status(400).json({
        success: false,
        message: "User already registered"
      })
    }

    let locationData

    if (role === "DOCTOR") {
      const { lat, lng } = await geocodeCity(city)

      locationData = {
        type: "Point",
        coordinates: [lng, lat]
      }
    }

    if (role === "PATIENT") {
      delete req.body.location
    }

    const normalizedSpeciality =
      role === "DOCTOR"
        ? speciality.trim().toLowerCase()
        : undefined

    const user = await User.create({
      role,
      name,
      email,
      mobileNumber,
      password,
      gender,
      dob,
      fee: role === "DOCTOR" ? fee : undefined,
      speciality: normalizedSpeciality,
      city: role === "DOCTOR" ? city : undefined,
      address: role === "DOCTOR" ? address : undefined,
      location: role === "DOCTOR" ? locationData : undefined
    })


    const accessToken = generateAccessToken(user)
    const refreshToken = generateRefreshToken(user)

    res.cookie("refreshToken", refreshToken, {
      httpOnly: true,
      sameSite: "strict",
      secure: process.env.NODE_ENV === "production",
      maxAge: 7 * 24 * 60 * 60 * 1000
    })

    return res.status(201).json({
      success: true,
      data: {
        accessToken
      }
    })
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: error.message
    })
  }
}

exports.login = async (req, res) => {
  try {
    const { email, password } = req.body

    if (!email || !password) {
      return res.status(400).json({
        success: false,
        message: "Email and password required"
      })
    }

    const user = await User.findOne({ email })
    if (!user) {
      return res.status(401).json({
        success: false,
        message: "Invalid credentials"
      })
    }

    const isMatch = await bcrypt.compare(password, user.password)
    if (!isMatch) {
      return res.status(401).json({
        success: false,
        message: "Invalid credentials"
      })
    }

    const accessToken = generateAccessToken(user)
    const refreshToken = generateRefreshToken(user)

    res.cookie("refreshToken", refreshToken, {
      httpOnly: true,
      sameSite: "strict",
      secure: process.env.NODE_ENV === "production",
      maxAge: 7 * 24 * 60 * 60 * 1000
    })

    res.json({
      success: true,
      data: {
        accessToken
      }
    })
  } catch (err) {
    res.status(500).json({
      success: false,
      message: err.message
    })
  }
}

exports.refreshToken = async (req, res) => {
  try {
    const token = req.cookies.refreshToken
    if (!token) {
      return res.status(401).json({ message: "Refresh token missing" })
    }

    const decoded = jwt.verify(token, process.env.JWT_REFRESH_SECRET)
    const user = await User.findById(decoded.id)

    if (!user) {
      return res.status(401).json({ message: "User not found" })
    }

    const newAccessToken = generateAccessToken(user)

    res.json({
      success: true,
      data: {
        accessToken: newAccessToken
      }
    })
  } catch (error) {
    return res.status(401).json({ message: "Invalid refresh token" })
  }
}

