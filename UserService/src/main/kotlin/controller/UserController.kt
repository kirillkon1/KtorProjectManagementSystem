@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.itmo.dto.CreateUserRequest
import ru.itmo.service.UserService

private val logger: Logger = LoggerFactory.getLogger("ru.itmo.controller.UserRoutes")

fun Route.userRoutes(userService: UserService) {
    route("api/users") {
        // Создание пользователя
        post {
            // Получаем JSON-объект и десериализуем его в data class
            val request = call.receive<CreateUserRequest>()

            // Валидация обязательных полей (если необходимо, но в данном случае data class обеспечит их наличие)
            if (request.username.isBlank() || request.password.isBlank() || request.email.isBlank()) {
                return@post call.respondText("Missing required fields", status = HttpStatusCode.BadRequest)
            }

            // Создаем пользователя
            val user =
                userService.createUser(
                    request.username,
                    request.password,
                    request.email,
                    request.firstName,
                    request.lastName,
                )
            call.respond(HttpStatusCode.Created, user)
        }

        // Получение пользователя по ID
        get("{id}") {
            logger.info("Received GET /users/{id} request")
            val id =
                call.parameters["id"]?.toLongOrNull() ?: run {
                    logger.warn("Invalid ID parameter")
                    return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                }
            val user = userService.getUserById(id)
            if (user == null) {
                logger.warn("User not found for id: $id")
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            } else {
                logger.info("User found for id: $id")
                call.respond(user)
            }
        }

        // Получение всех пользователей
        get {
            logger.info("Received GET /users request")
            val users = userService.getAllUsers()
            logger.info("Returning ${users.size} users")
            call.respond(users)
        }

        // Обновление пользователя
        put("{id}") {
            logger.info("Received PUT /users/{id} request")
            val id =
                call.parameters["id"]?.toLongOrNull() ?: run {
                    logger.warn("Invalid ID parameter for update")
                    return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                }
            val params = call.receiveParameters()
            val username = params["username"]
            val password = params["password"]
            val email = params["email"]
            val firstName = params["firstName"]
            val lastName = params["lastName"]
            logger.info("Updating user with id: $id")
            val updatedUser = userService.updateUser(id, username, password, email, firstName, lastName)
            if (updatedUser == null) {
                logger.warn("User not found for update with id: $id")
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            } else {
                logger.info("User updated with id: $id")
                call.respond(updatedUser)
            }
        }

        // Удаление пользователя
        delete("{id}") {
            logger.info("Received DELETE /users/{id} request")
            val id =
                call.parameters["id"]?.toLongOrNull() ?: run {
                    logger.warn("Invalid ID parameter for delete")
                    return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                }
            logger.info("Deleting user with id: $id")
            if (userService.deleteUser(id)) {
                logger.info("User deleted with id: $id")
                call.respondText("User deleted", status = HttpStatusCode.OK)
            } else {
                logger.warn("User not found for deletion with id: $id")
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
