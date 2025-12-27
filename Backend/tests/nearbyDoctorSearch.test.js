require("dotenv").config()
jest.setTimeout(30000)

const request = require("supertest")
const mongoose = require("mongoose")
const { MongoMemoryServer } = require("mongodb-memory-server")

const app = require("../src/app")
const User = require("../src/models/User")

let mongoServer

describe("Nearby Doctor Search API", () => {

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create()
    const uri = mongoServer.getUri()
    await mongoose.connect(uri)

    await User.create([
      {
        role: "DOCTOR",
        name: "Dr Near",
        email: "near@test.com",
        password: "123456",
        mobileNumber: "9991111111",
        speciality: "cardiologist",
        fee: 500,
        location: {
          type: "Point",
          coordinates: [77.2090, 28.6139] // Delhi
        }
      },
      {
        role: "DOCTOR",
        name: "Dr Far",
        email: "far@test.com",
        password: "123456",
        mobileNumber: "9992222222",
        speciality: "orthopedic",
        fee: 700,
        location: {
          type: "Point",
          coordinates: [77.3910, 28.5355] // Noida
        }
      }
    ])

    // Ensure geo index exists
    await User.collection.createIndex({ location: "2dsphere" })
  })

  it("should return nearby doctors filtered by speciality with distance", async () => {
    const res = await request(app)
      .get("/api/doctors/nearby")
      .query({
        lat: 28.6139,
        lng: 77.2090,
        distance: 10,
        speciality: "cardiologist"
      })

    expect(res.statusCode).toBe(200)
    expect(res.body.success).toBe(true)
    expect(Array.isArray(res.body.data)).toBe(true)
    expect(res.body.data.length).toBe(1)

    const doctor = res.body.data[0]
    expect(doctor.speciality).toBe("cardiologist")
    expect(doctor.distance).toBeDefined()
    expect(typeof doctor.distance).toBe("number")
  })

  afterAll(async () => {
    await mongoose.disconnect()
    await mongoServer.stop()
  })
})