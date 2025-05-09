package com.dang.boswos_firebase.data

data class UserProfile(
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null
)