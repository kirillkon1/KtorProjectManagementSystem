package model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object ProjectsTable : LongIdTable("projects") {
    val name: Column<String> = varchar("name", 255)
    val description: Column<String?> = text("description").nullable()
    val location: Column<String?> = varchar("location", 255).nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    val budget = double("budget").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").nullable()
}

@Serializable
data class ProjectDTO(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val location: String? = null,
    // Даты передаются в виде строки в формате ISO
    val startDate: String? = null,
    val endDate: String? = null,
    val budget: Double? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

class ProjectEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ProjectEntity>(ProjectsTable)

    var name by ProjectsTable.name
    var description by ProjectsTable.description
    var location by ProjectsTable.location
    var startDate by ProjectsTable.startDate
    var endDate by ProjectsTable.endDate
    var budget by ProjectsTable.budget
    var createdAt by ProjectsTable.createdAt
    var updatedAt by ProjectsTable.updatedAt

    fun toDTO(): ProjectDTO =
        ProjectDTO(
            id = id.value,
            name = name,
            description = description,
            location = location,
            startDate = startDate?.toString(),
            endDate = endDate?.toString(),
            budget = budget,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt?.toString()
        )
}
