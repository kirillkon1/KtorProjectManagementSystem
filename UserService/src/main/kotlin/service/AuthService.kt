package ru.itmo.service

interface AuthService {
    /**
     * Пытается авторизовать пользователя по логину и паролю.
     * Возвращает JWT-токен, если аутентификация успешна, или null в противном случае.
     */
    suspend fun login(username: String, password: String): String?


    /**
     * Регистрация пользователя по логину и паролю.
     */
    suspend fun register(username: String, password: String, email: String,
        firstName: String, lastName: String): String?

    /**
     * Проверяет валидность переданного токена.
     */
    suspend fun verifyToken(token: String): Boolean
}