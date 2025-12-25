const express = require("express")
const router = express.Router()
const { bookAppointment } = require("../controllers/appointmentController")
const auth = require("../middleware/authMiddleware")

router.post("/book", auth, bookAppointment)

module.exports = router
