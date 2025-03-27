package model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

// Таблица пользователей
object UsersTable : LongIdTable("users") {
    val username = varchar("username", 50)
    val password = varchar("password", 64)
    val email = varchar("email", 100).nullable()
    val firstName = varchar("first_name", 50).nullable()
    val lastName = varchar("last_name", 50).nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").nullable()
}

// Сериализуемая модель для API
@Serializable
data class UserDTO(
    val id: Long? = null,
    val username: String,
    val password: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    // Для упрощения даты передаются как строка в формате ISO
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

// Сущность для работы с базой данных через Exposed DAO
class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var username by UsersTable.username
    var password by UsersTable.password
    var email by UsersTable.email
    var firstName by UsersTable.firstName
    var lastName by UsersTable.lastName
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt

    // Преобразование в DTO
    fun toDTO(): UserDTO =
        UserDTO(
            id = id.value,
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt?.toString(),
        )
}
