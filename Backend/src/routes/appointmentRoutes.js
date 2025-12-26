const express = require("express")
const router = express.Router()
const { bookAppointment, getMyAppointments } = require("../controllers/appointmentController")
const {protect} = require("../middlewares/auth.middleware")

router.post("/book", protect, bookAppointment)
router.get("/my", protect, getMyAppointments)

module.exports = router
