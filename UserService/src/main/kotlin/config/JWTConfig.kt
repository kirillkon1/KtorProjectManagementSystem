package ru.itmo.config

import io.ktor.server.application.*

data class JWTConfig(
    val issuer: String,
    val audience: String,
    val secret: String,
    val validityInMs: Long
)

fun Application.jwtConfig(): JWTConfig {
    val config = environment.config
    return JWTConfig(
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        secret = config.property("jwt.secret").getString(),
        validityInMs = config.property("jwt.validityInMs").getString().toLong()
    )
}