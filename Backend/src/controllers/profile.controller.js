const User = require("../models/User")

// GET PROFILE
exports.getProfile = async (req, res) => {
  try {
    const user = await User.findById(req.user._id).select("-password")

    if (!user) {
      return res.status(404).json({
        success: false,
        message: "User not found"
      })
    }

    return res.json({
      success: true,
      data: {
        image: null, 
        name: user.name,
        email: user.email,
        gender: user.gender,
        role: user.role,
        speciality: user.role === "DOCTOR" ? user.speciality : undefined,
        location: user.location
      }
    })
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: error.message
    })
  }
}

// UPDATE PROFILE
exports.updateProfile = async (req, res) => {
  try {
    const updates = req.body

    const user = await User.findByIdAndUpdate(
      req.user._id,
      updates,
      { new: true }
    ).select("-password")

    return res.json({
      success: true,
      data: user
    })
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: error.message
    })
  }
}
