const express = require("express")
const router = express.Router()
const {
  getDoctorsBySpeciality,
  getNearbyDoctors
} = require("../controllers/doctorController")

router.get("/speciality", getDoctorsBySpeciality)
router.get("/nearby", getNearbyDoctors)

module.exports = router
