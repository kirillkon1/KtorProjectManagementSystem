@file:Suppress("ktlint:standard:filename")

package repository

import kotlinx.coroutines.Dispatchers
import model.UserDTO
import model.UserEntity
import model.UsersTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class UserRepository {
    suspend fun createUser(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
    ): UserDTO =
        newSuspendedTransaction(Dispatchers.IO) {
            val userEntity =
                UserEntity.new {
                    this.username = username
                    this.password = password
                    this.email = email
                    this.firstName = firstName
                    this.lastName = lastName
                    this.createdAt = LocalDateTime.now()
                    this.updatedAt = LocalDateTime.now()
                }
            userEntity.toDTO()
        }

    suspend fun getUserById(id: Long): UserDTO? =
        newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.findById(id)?.toDTO()
        }

    suspend fun getAllUsers(): List<UserDTO> =
        newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.all().map { it.toDTO() }
        }

    // Пример асинхронного метода для получения пользователя по username
    suspend fun getUserByUsername(username: String): UserDTO? = newSuspendedTransaction(Dispatchers.IO) {
        UserEntity.find { UsersTable.username eq username }.firstOrNull()?.toDTO()
    }

    suspend fun updateUser(
        id: Long,
        username: String?,
        password: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
    ): UserDTO? =
        newSuspendedTransaction(Dispatchers.IO) {
            val userEntity = UserEntity.findById(id) ?: return@newSuspendedTransaction null

            if (username != null) userEntity.username = username
            if (password != null) userEntity.password = password // Предполагается, что пароль уже захеширован
            if (email != null) userEntity.email = email
            if (firstName != null) userEntity.firstName = firstName
            if (lastName != null) userEntity.lastName = lastName
            userEntity.updatedAt = LocalDateTime.now()

            userEntity.toDTO()
        }

    suspend fun deleteUser(id: Long): Boolean =
        newSuspendedTransaction(Dispatchers.IO) {
            val userEntity = UserEntity.findById(id)
            if (userEntity != null) {
                userEntity.delete()
                true
            } else {
                false
            }
        }

}
