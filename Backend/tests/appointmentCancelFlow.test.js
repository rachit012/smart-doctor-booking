require("dotenv").config()
jest.setTimeout(30000)

const request = require("supertest")
const mongoose = require("mongoose")
const jwt = require("jsonwebtoken")
const { MongoMemoryServer } = require("mongodb-memory-server")

const app = require("../src/app")
const User = require("../src/models/User")
const Availability = require("../src/models/Availability")
const Appointment = require("../src/models/Appointment")

let mongoServer
let patientToken
let doctorToken
let doctorId
let appointmentId

describe("Appointment Cancellation Flow", () => {

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create()
    const uri = mongoServer.getUri()

    await mongoose.connect(uri)

    const doctor = await User.create({
      role: "DOCTOR",
      name: "Dr Cancel",
      email: "drcancel@test.com",
      password: "123456",
      mobileNumber: "9999991111",
      speciality: "cardiologist",
      fee: 600,
      location: {
        type: "Point",
        coordinates: [77.2090, 28.6139]
      }
    })

    doctorId = doctor._id

    const patient = await User.create({
      role: "PATIENT",
      name: "Patient Cancel",
      email: "patientcancel@test.com",
      password: "123456",
      mobileNumber: "8888881111"
    })

    doctorToken = jwt.sign(
      { id: doctor._id, role: "DOCTOR" },
      process.env.JWT_SECRET
    )

    patientToken = jwt.sign(
      { id: patient._id, role: "PATIENT" },
      process.env.JWT_SECRET
    )

    await Availability.create({
      doctorId,
      date: "2026-01-12",
      slots: [
        { startTime: "11:00", endTime: "11:30", isBooked: false }
      ]
    })

    const appointment = await Appointment.create({
      doctorId,
      patientId: patient._id,
      date: "2026-01-12",
      startTime: "11:00",
      endTime: "11:30",
      status: "BOOKED"
    })

    appointmentId = appointment._id

    await Availability.updateOne(
      {
        doctorId,
        date: "2026-01-12",
        "slots.startTime": "11:00"
      },
      {
        $set: { "slots.$.isBooked": true }
      }
    )
  })

  it("patient should cancel appointment", async () => {
    const res = await request(app)
      .patch(`/api/appointments/cancel/${appointmentId}`)
      .set("Authorization", `Bearer ${patientToken}`)

    expect(res.statusCode).toBe(200)
    expect(res.body.success).toBe(true)
  })

  it("appointment status should be CANCELLED", async () => {
    const appointment = await Appointment.findById(appointmentId)
    expect(appointment.status).toBe("CANCELLED")
  })

  it("slot should be marked as not booked after cancellation", async () => {
    const availability = await Availability.findOne({
      doctorId,
      date: "2026-01-12"
    })

    expect(availability.slots[0].isBooked).toBe(false)
  })

  afterAll(async () => {
    await mongoose.disconnect()
    await mongoServer.stop()
  })
})