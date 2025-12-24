const express = require("express")
const cors = require("cors")
const { protect } = require("./middlewares/auth.middleware")

const app = express()

app.use(cors())
app.use(express.json())


app.use("/api/auth", require("./routes/auth.routes"))
app.use("/api/profile", require("./routes/profile.routes"))

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
