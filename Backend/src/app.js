const express = require("express")
const cors = require("cors")
const { protect } = require("./middlewares/auth.middleware")
const cookieParser = require("cookie-parser")

const app = express()
app.use(cookieParser())
app.use(cors({
  origin: [
    "http://localhost:5173",      
    "https://your-frontend.app"   
  ],
  credentials: true
}))

app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.use("/api/auth", require("./routes/auth.routes"))
app.use("/api/profile", require("./routes/profile.routes"))
app.use("/api/availability", require("./routes/availabilityRoutes"))
app.use("/api/appointments", require("./routes/appointmentRoutes"))
app.use("/api/doctors", require("./routes/doctorRoutes"))

app.get("/", (req, res) => {
  res.json({ success: true, message: "API running" })
})

app.get("/api/test-protected", protect, (req, res) => {
  res.json({
    success: true,
    user: req.user
  })
})

module.exports = app
