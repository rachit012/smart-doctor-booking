const mongoose = require("mongoose")

const appointmentSchema = new mongoose.Schema({
  doctorId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true
  },
  patientId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true
  },
  date: String,
  startTime: String,
  endTime: String,

  status: {
    type: String,
    enum: ["BOOKED", "CANCELLED", "COMPLETED"],
    default: "BOOKED"
  }
}, { timestamps: true })

module.exports = mongoose.model("Appointment", appointmentSchema)
