// файл: src/main/kotlin/ru/itmo/userservice/service/UserService.kt
package ru.itmo.service

import model.UserDTO

interface UserService {
    suspend fun createUser(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
    ): UserDTO

    suspend fun getUserById(id: Long): UserDTO?

    suspend fun getAllUsers(): List<UserDTO>

    suspend fun updateUser(
        id: Long,
        username: String? = null,
        password: String? = null,
        email: String? = null,
        firstName: String? = null,
        lastName: String? = null,
    ): UserDTO?

    suspend fun deleteUser(id: Long): Boolean
}
