const mongoose = require("mongoose")
const bcrypt = require("bcryptjs")

const userSchema = new mongoose.Schema(
  {
    role: {
      type: String,
      enum: ["DOCTOR", "PATIENT"],
      required: true
    },

    name: {
      type: String,
      required: true
    },

    email: {
      type: String,
      required: true,
      unique: true
    },

    mobileNumber: {
      type: String,
      required: true,
      unique: true
    },

    password: {
      type: String,
      required: true
    },

    gender: {
      type: String,
      enum: ["Male", "Female", "Other"]
    },

    dob: {
      type: Date
    },

    // Doctor-only fields
    fee: {
      type: Number
    },

    speciality: {
      type: String
    },
    location: {
      type: {
        type: String,
        enum: ["Point"],
        default: "Point"
      },
      coordinates: {
        type: [Number],
        required: function () {
          return this.role === "DOCTOR"
        }
      }
    }
  },
  { timestamps: true }
)

userSchema.index({ location: "2dsphere" })

userSchema.pre("save", async function () {
  if (!this.isModified("password")) return
  this.password = await bcrypt.hash(this.password, 10)
})


module.exports = mongoose.model("User", userSchema)
