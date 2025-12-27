const User = require("../models/User")
const Availability = require("../models/Availability")

exports.getDoctorsBySpeciality = async (req, res) => {
  try {
    const { speciality, date } = req.query

    const doctors = await User.find({
      role: "DOCTOR",
      speciality
    }).select("-password")

    if (!date) {
      return res.json({ success: true, data: doctors })
    }

    const doctorIds = doctors.map(d => d._id)

    const availability = await Availability.find({
      doctorId: { $in: doctorIds },
      date,
      "slots.isBooked": false
    })

    const availableDoctorIds = availability.map(a => a.doctorId.toString())

    const filteredDoctors = doctors.filter(d =>
      availableDoctorIds.includes(d._id.toString())
    )

    res.json({ success: true, data: filteredDoctors })
  } catch (error) {
    res.status(500).json({ message: error.message })
  }
}

exports.getNearbyDoctors = async (req, res) => {
  try {
    const { lat, lng, distance = 5, speciality } = req.query

    if (!lat || !lng) {
      return res.status(400).json({
        success: false,
        message: "Latitude and longitude are required"
      })
    }

    const pipeline = [
      {
        $geoNear: {
          near: {
            type: "Point",
            coordinates: [Number(lng), Number(lat)]
          },
          distanceField: "distance", 
          maxDistance: Number(distance) * 1000,
          spherical: true,
          query: {
            role: "DOCTOR",
            "location.type": "Point",
            ...(speciality && {
              speciality: speciality.trim().toLowerCase()
            })
          }
        }
      },
      {
        $project: {
          password: 0
        }
      }
    ]

    const doctors = await User.aggregate(pipeline)

    const formattedDoctors = doctors.map(doc => ({
      ...doc,
      distance: Number((doc.distance / 1000).toFixed(2))
    }))

    res.json({
      success: true,
      data: formattedDoctors
    })
  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    })
  }
}

