require("dotenv").config()
jest.setTimeout(30000)

const request = require("supertest")
const mongoose = require("mongoose")
const jwt = require("jsonwebtoken")
const { MongoMemoryServer } = require("mongodb-memory-server")

const app = require("../src/app")
const User = require("../src/models/User")

let mongoServer
let userToken
let userId

describe("Get Profile API", () => {

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create()
    const uri = mongoServer.getUri()

    await mongoose.connect(uri)

    const user = await User.create({
      role: "PATIENT",
      name: "Profile Test",
      email: "profile@test.com",
      password: "123456",
      mobileNumber: "9999990000",
      gender: "Male"
    })

    userId = user._id

    userToken = jwt.sign(
      { id: userId, role: "PATIENT" },
      process.env.JWT_SECRET
    )
  })

  it("should fetch logged-in user profile", async () => {
    const res = await request(app)
      .get("/api/profile")
      .set("Authorization", `Bearer ${userToken}`)

    expect(res.statusCode).toBe(200)
    expect(res.body.success).toBe(true)
    expect(res.body.data.name).toBe("Profile Test")
    expect(res.body.data.email).toBe("profile@test.com")
  })

  afterAll(async () => {
    await mongoose.disconnect()
    await mongoServer.stop()
  })
})