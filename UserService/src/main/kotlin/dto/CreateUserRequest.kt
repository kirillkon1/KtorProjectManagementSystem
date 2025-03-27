package ru.itmo.dto

data class CreateUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String = "",
    val lastName: String = "",
)
