const Availability = require("../models/Availability")

exports.addAvailability = async (req, res) => {
  try {
    const { date, slots } = req.body

    const existing = await Availability.findOne({
      doctorId: req.user._id,
      date
    })

    if (existing) {
      return res.status(400).json({
        success: false,
        message: "Availability already added for this date"
      })
    }

    const availability = await Availability.create({
      doctorId: req.user._id,
      date,
      slots
    })

    res.status(201).json({
      success: true,
      data: availability
    })
  } catch (error) {
    res.status(500).json({ message: error.message })
  }
}

exports.getAvailability = async (req, res) => {
  try {
    const { doctorId, date } = req.query

    const availability = await Availability.findOne({ doctorId, date })

    if (!availability) {
      return res.json({
        success: true,
        data: {
          doctorId,
          date,
          slots: []
        }
      })
    }

    return res.json({
      success: true,
      data: {
        doctorId: availability.doctorId,
        date: availability.date,
        slots: availability.slots || []
      }
    })
  } catch (error) {
    res.status(500).json({ message: error.message })
  }
}
