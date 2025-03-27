package ru.itmo.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object TasksTable : LongIdTable("tasks") {
    val title: Column<String> = varchar("title", 255)
    val description: Column<String?> = text("description").nullable()
    val status = enumerationByName("status", 50, TaskStatus::class)
    val priority = enumerationByName("priority", 50, TaskPriority::class)
    val assignedTo = long("assign_to") // ID пользователя из User Management Service
    val projectId = long("project_id")   // ID проекта из Project Management Service
    val dueDate = datetime("due_date").nullable()

    val createdAt = datetime("created_at").nullable().default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").nullable()
}

@Serializable
data class TaskDTO(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val status: TaskStatus,
    val priority: TaskPriority,
    val assignedTo: Long,
    val projectId: Long,
    val dueDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

enum class TaskStatus {
    NEW,
    IN_PROGRESS,
    IN_TESTING,
    DONE,
}

enum class TaskPriority {
    LOW,
    NORMAL,
    HIGH,
    CRITICAL,
}

class TaskEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskEntity>(TasksTable)

    var title by TasksTable.title
    var description by TasksTable.description
    var status by TasksTable.status
    var priority by TasksTable.priority
    var assignedTo by TasksTable.assignedTo
    var projectId by TasksTable.projectId
    var dueDate by TasksTable.dueDate
    var createdAt by TasksTable.createdAt
    var updatedAt by TasksTable.updatedAt

    fun toDTO(): TaskDTO =
        TaskDTO(
            id = id.value,
            title = title,
            description = description,
            status = status,
            priority = priority,
            assignedTo = assignedTo,
            projectId = projectId,
            dueDate = dueDate?.toString(),
            createdAt = createdAt?.toString(),
            updatedAt = updatedAt?.toString()
        )
}