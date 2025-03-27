// файл: src/main/kotlin/ru/itmo/userservice/service/UserServiceImpl.kt
package ru.itmo.service.impl

import model.UserDTO
import org.mindrot.jbcrypt.BCrypt
import repository.UserRepository
import ru.itmo.service.UserService

class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {
    override suspend fun createUser(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
    ): UserDTO {
        // Хешируем пароль перед сохранением
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        return userRepository.createUser(username, hashedPassword, email, firstName, lastName)
    }

    override suspend fun getUserById(id: Long): UserDTO? = userRepository.getUserById(id)

    override suspend fun getAllUsers(): List<UserDTO> = userRepository.getAllUsers()

    override suspend fun updateUser(
        id: Long,
        username: String?,
        password: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
    ): UserDTO? {
        val hashedPassword = password?.let { BCrypt.hashpw(it, BCrypt.gensalt()) }
        return userRepository.updateUser(id, username, hashedPassword, email, firstName, lastName)
    }

    override suspend fun deleteUser(id: Long): Boolean = userRepository.deleteUser(id)
}
