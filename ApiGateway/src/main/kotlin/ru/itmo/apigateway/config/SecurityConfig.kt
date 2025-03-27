package ru.itmo.apigateway.config

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke // Kotlin DSL-расширения
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    @Value("\${spring.security.oauth2.resourceserver.jwt.secret}")
    private val secret: String,
) {
    @Bean
    fun reactiveJwtDecoder(): ReactiveJwtDecoder {
        val keyBytes = Decoders.BASE64.decode(secret)
        val key = Keys.hmacShaKeyFor(keyBytes)
        return NimbusReactiveJwtDecoder.withSecretKey(key).build()
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            // Выключить CSRF:
            csrf { disable() }

            // Настройка авторизации запросов
            authorizeExchange {
                authorize("/api/auth/**", permitAll)
                authorize("/actuator/**", permitAll)
                authorize(anyExchange, authenticated)
            }

            // Подключаем OAuth2 Resource Server с JWT
            oauth2ResourceServer {
                jwt { }
            }
        }
    }
}
