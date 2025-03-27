@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.jsonwebtoken.io.Decoders
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    // Получаем настройки JWT из конфигурации приложения
    val jwtConfig = jwtConfig()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.audience // или другое значение, если требуется
            val keyBytes = Decoders.BASE64.decode(jwtConfig.secret)
            verifier(
                JWT.require(Algorithm.HMAC256(keyBytes))
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
