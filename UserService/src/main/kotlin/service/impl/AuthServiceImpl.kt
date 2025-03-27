package ru.itmo.service.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.jsonwebtoken.io.Decoders
import org.mindrot.jbcrypt.BCrypt
import repository.UserRepository
import ru.itmo.config.JWTConfig
import ru.itmo.service.AuthService
import java.util.Date

class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtConfig: JWTConfig // Конфигурация JWT внедряется через конструктор
) : AuthService {

    override suspend fun login(username: String, password: String): String? {
        // Получаем пользователя по username
        val user = userRepository.getUserByUsername(username) ?: return null

        // Проверяем пароль с помощью BCrypt
        if (!BCrypt.checkpw(password, user.password)) return null

        val keyBytes = Decoders.BASE64.decode(jwtConfig.secret)

        // Создаём JWT-токен, используя параметры из jwtConfig
        return JWT.create()
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.validityInMs))
            .sign(Algorithm.HMAC256(keyBytes))
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String
    ): String? {
        // Проверяем, существует ли уже пользователь с таким именем
        if (userRepository.getUserByUsername(username) != null) {
            // Можно выбросить исключение или вернуть null
            return null
        }

        // Хешируем пароль
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        // Создаём пользователя через репозиторий
        userRepository.createUser(username, hashedPassword, email, firstName, lastName)

        val keyBytes = Decoders.BASE64.decode(jwtConfig.secret)

        // Генерируем JWT-токен для нового пользователя
        return JWT.create()
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.validityInMs))
            .sign(Algorithm.HMAC256(keyBytes))
    }

    override suspend fun verifyToken(token: String): Boolean {
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(jwtConfig.secret))
                .withAudience(jwtConfig.audience)
                .withIssuer(jwtConfig.issuer)
                .build()
            verifier.verify(token)
            true
        } catch (ex: Exception) {
            false
        }
    }
}