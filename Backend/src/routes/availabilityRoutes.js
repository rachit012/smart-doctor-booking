const express = require("express")
const router = express.Router()
const {
  addAvailability,
  getAvailability
} = require("../controllers/availabilityController")
const auth = require("../middleware/authMiddleware")

router.post("/add", auth, addAvailability)
router.get("/", getAvailability)

module.exports = router
