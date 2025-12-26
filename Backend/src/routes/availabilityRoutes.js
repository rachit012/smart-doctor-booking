const express = require("express")
const router = express.Router()
const {
  addAvailability,
  getAvailability
} = require("../controllers/availabilityController")
const {protect} = require("../middlewares/auth.middleware")

router.post("/add", protect, addAvailability)
router.get("/", getAvailability)

module.exports = router