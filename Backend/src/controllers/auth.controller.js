const User = require("../models/User")
const jwt = require("jsonwebtoken")
const bcrypt = require("bcryptjs")

const generateToken = (user) => {
  return jwt.sign(
    { id: user._id, role: user.role },
    process.env.JWT_SECRET,
    { expiresIn: process.env.JWT_EXPIRES_IN }
  )
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
      location
    } = req.body

    // Basic validation
    if (!role || !name || !email || !mobileNumber || !password) {
      return res.status(400).json({
        success: false,
        message: "Required fields missing"
      })
    }

    // Role-based validation
    if (role === "DOCTOR") {
      if (!fee || !speciality || !location) {
        return res.status(400).json({
          success: false,
          message: "Doctor must provide fee, speciality and location"
        })
      }
    }

    // Check if user already exists
    const existingUser = await User.findOne({ mobileNumber })
    if (existingUser) {
      return res.status(400).json({
        success: false,
        message: "User already registered"
      })
    }

    // Create user
    await User.create({
      role,
      name,
      email,
      mobileNumber,
      password,
      gender,
      dob,
      fee: role === "DOCTOR" ? fee : undefined,
      speciality: role === "DOCTOR" ? speciality : undefined,
      location: role === "DOCTOR" ? location : undefined
    })

    return res.status(201).json({
      success: true,
      data: {}
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
    const { role, mobileNumber, password } = req.body

    if (!role || !mobileNumber || !password) {
      return res.status(400).json({
        success: false,
        message: "Required fields missing"
      })
    }

    const user = await User.findOne({ mobileNumber, role })
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

    const token = generateToken(user)

    return res.json({
      success: true,
      data: {
        accessToken: token
      }
    })
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: error.message
    })
  }
}
