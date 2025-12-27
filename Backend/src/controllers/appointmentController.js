const Availability = require("../models/Availability")
const Appointment = require("../models/Appointment")

exports.bookAppointment = async (req, res) => {
  try {
    const { doctorId, date, startTime, endTime } = req.body
    const patientId = req.user._id

    const patientConflict = await Appointment.findOne({
      patientId,
      date,
      startTime,
      status: "BOOKED"
    })

    if (patientConflict) {
      return res.status(400).json({
        message: "You already have an appointment at this time"
      })
    }

    const availability = await Availability.findOneAndUpdate(
      {
        doctorId,
        date,
        "slots.startTime": startTime,
        "slots.isBooked": false
      },
      {
        $set: { "slots.$.isBooked": true }
      },
      { new: true }
    )

    if (!availability) {
      return res.status(400).json({
        message: "Slot already booked or unavailable"
      })
    }

    const appointment = await Appointment.create({
      doctorId,
      patientId,
      date,
      startTime,
      endTime
    })

    res.status(201).json({
      success: true,
      message: "Appointment booked successfully",
      data: appointment
    })
  } catch (error) {
    res.status(500).json({ message: error.message })
  }
}

exports.getMyAppointments = async (req, res) => {
  try {
    const appointments = await Appointment.find({
      patientId: req.user._id
    }).sort({ date: 1, startTime: 1 })

    res.json({
      success: true,
      data: appointments
    })
  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    })
  }
}

exports.getDoctorAppointments = async (req, res) => {
  try {
    if (req.user.role !== "DOCTOR") {
      return res.status(403).json({
        success: false,
        message: "Access denied"
      })
    }

    const appointments = await Appointment.find({
      doctorId: req.user._id
    }).sort({ date: 1, startTime: 1 })

    res.json({
      success: true,
      data: appointments
    })
  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    })
  }
}

