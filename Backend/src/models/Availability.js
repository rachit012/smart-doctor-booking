const mongoose = require("mongoose")

const slotSchema = new mongoose.Schema({
  startTime: String,
  endTime: String,
  isBooked: {
    type: Boolean,
    default: false
  }
})

const availabilitySchema = new mongoose.Schema({
  doctorId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true
  },
  date: {
    type: String,
    required: true
  },
  slots: [slotSchema]
})

module.exports = mongoose.model("Availability", availabilitySchema)
