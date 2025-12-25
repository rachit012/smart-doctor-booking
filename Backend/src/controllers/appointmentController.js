const Availability = require("../models/Availability")
const Appointment = require("../models/Appointment")

exports.bookAppointment = async (req, res) => {
  try {
    const { doctorId, date, startTime, endTime } = req.body
    const patientId = req.user._id

    // 1️⃣ Check patient conflict
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

    // 2️⃣ Atomically book slot
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

    // 3️⃣ Create appointment
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
