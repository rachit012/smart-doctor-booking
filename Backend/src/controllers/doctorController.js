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
    const { lat, lng, speciality } = req.query

    const query = {
      role: "DOCTOR",
      location: {
        $near: {
          $geometry: {
            type: "Point",
            coordinates: [Number(lng), Number(lat)]
          },
          $maxDistance: 10000 
        }
      }
    }

    if (speciality) query.speciality = speciality

    const doctors = await User.find(query).select("-password")

    res.json({ success: true, data: doctors })
  } catch (error) {
    res.status(500).json({ message: error.message })
  }
}
