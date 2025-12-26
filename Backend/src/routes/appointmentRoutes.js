const express = require("express")
const router = express.Router()
const { bookAppointment } = require("../controllers/appointmentController")
const {protect} = require("../middlewares/auth.middleware")

router.post("/book", protect, bookAppointment)

module.exports = router
