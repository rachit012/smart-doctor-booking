const express = require("express")
const router = express.Router()
const { bookAppointment, getMyAppointments, getDoctorAppointments } = require("../controllers/appointmentController")
const {protect} = require("../middlewares/auth.middleware")

router.post("/book", protect, bookAppointment)
router.get("/my", protect, getMyAppointments)
router.get("/doctor", protect, getDoctorAppointments)
module.exports = router
