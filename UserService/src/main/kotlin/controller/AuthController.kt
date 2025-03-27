// файл: src/main/kotlin/ru/itmo/userservice/controller/AuthController.kt
package controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itmo.dto.LoginDto
import ru.itmo.dto.RegisterDto
import ru.itmo.service.AuthService


fun Route.authRoutes(authService: AuthService) {
    route("/api/auth") {
        // Эндпоинт для регистрации с использованием DTO
        post("/register") {
            // Читаем запрос в виде RegisterRequest
            val registerRequest = call.receive<RegisterDto>()

            // Здесь можно добавить валидацию registerRequest, если не используется плагин
            call.application.environment.log.info("Registering user: ${registerRequest.username}")
            val token = authService.register(
                registerRequest.username,
                registerRequest.password,
                registerRequest.email,
                registerRequest.firstName,
                registerRequest.lastName
            )
            if (token == null) {
                call.application.environment.log.error("Registration failed for user: ${registerRequest.username}")
                call.respond(HttpStatusCode.Conflict, "User already exists or registration failed")
            } else {
                call.application.environment.log.info("User registered successfully: ${registerRequest.username}")
                call.respond(HttpStatusCode.Created, mapOf("token" to token))
            }
        }

        // Эндпоинт для логина с использованием DTO
        post("/login") {
            val loginRequest = call.receive<LoginDto>()
            call.application.environment.log.info("Login attempt for user: ${loginRequest.username}")
            val token = authService.login(loginRequest.username, loginRequest.password)
            if (token == null) {
                call.application.environment.log.warn("Login failed for user: ${loginRequest.username}")
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            } else {
                call.application.environment.log.info("User logged in successfully: ${loginRequest.username}")
                call.respond(HttpStatusCode.OK, mapOf("token" to token))
            }
        }

        // Эндпоинт для проверки токена
        post("/verify") {
            // Здесь можно использовать простой объект запроса или отдельный DTO, если требуется
            val params = call.receiveParameters()
            val token =
                params["token"] ?: return@post call.respondText("Missing token", status = HttpStatusCode.BadRequest)
            call.application.environment.log.info("Verifying token")
            val valid = authService.verifyToken(token)
            call.application.environment.log.info("Token verification result: $valid")
            call.respond(HttpStatusCode.OK, mapOf("valid" to valid))
        }
    }
}
