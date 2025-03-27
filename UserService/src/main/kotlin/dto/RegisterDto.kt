package ru.itmo.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
)