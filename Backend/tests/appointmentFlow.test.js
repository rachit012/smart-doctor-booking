require("dotenv").config()
jest.setTimeout(30000)

const request = require("supertest")
const mongoose = require("mongoose")
const jwt = require("jsonwebtoken")
const { MongoMemoryServer } = require("mongodb-memory-server")

const app = require("../src/app")
const User = require("../src/models/User")
const Availability = require("../src/models/Availability")

let mongoServer
let patientToken
let doctorToken
let doctorId

describe("Appointment Booking Flow (Patient + Doctor)", () => {

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create()
    const uri = mongoServer.getUri()

    await mongoose.connect(uri)

    const doctor = await User.create({
      role: "DOCTOR",
      name: "Dr Test",
      email: "dr@test.com",
      password: "123456",
      mobileNumber: "9999999999",
      speciality: "Cardiologist",
      fee: 500,
      location: {
        type: "Point",
        coordinates: [77.2090, 28.6139]
      }
    })

    doctorId = doctor._id

    const patient = await User.create({
      role: "PATIENT",
      name: "Patient Test",
      email: "patient@test.com",
      password: "123456",
      mobileNumber: "8888888888"
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
      date: "2026-01-10",
      slots: [
        { startTime: "10:00", endTime: "10:30", isBooked: false }
      ]
    })
  })

  it("patient should book appointment", async () => {
    const res = await request(app)
      .post("/api/appointments/book")
      .set("Authorization", `Bearer ${patientToken}`)
      .send({
        doctorId,
        date: "2026-01-10",
        startTime: "10:00",
        endTime: "10:30"
      })

    expect(res.statusCode).toBe(201)
  })

  it("patient should see his bookings", async () => {
    const res = await request(app)
      .get("/api/appointments/my")
      .set("Authorization", `Bearer ${patientToken}`)

    expect(res.statusCode).toBe(200)
  })

  it("doctor should see his bookings", async () => {
    const res = await request(app)
      .get("/api/appointments/doctor")
      .set("Authorization", `Bearer ${doctorToken}`)

    expect(res.statusCode).toBe(200)
  })

  afterAll(async () => {
    await mongoose.disconnect()
    await mongoServer.stop()
  })
})