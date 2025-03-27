package ru.itmo.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object TaskEventsTable : LongIdTable("task_analytics") {

    val title: Column<String> = varchar("title", 255)
    val description: Column<String?> = text("description").nullable()
    val assignedTo = long("assign_to").default(0)
    val projectId = long("project_id").default(0)
    val dueDate = datetime("due_date").nullable()

    val createdAt = datetime("created_at").nullable().default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").nullable().default(LocalDateTime.now())
}


@Serializable
data class TaskEventDTO(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val assignedTo: Long,
    val projectId: Long,
    val dueDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

class TaskEventEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskEventEntity>(TaskEventsTable)

    var title by TaskEventsTable.title
    var description by TaskEventsTable.description
    var assignedTo by TaskEventsTable.assignedTo
    var projectId by TaskEventsTable.projectId
    var dueDate by TaskEventsTable.dueDate
    var createdAt by TaskEventsTable.createdAt
    var updatedAt by TaskEventsTable.updatedAt

    fun toDTO(): TaskEventDTO =
        TaskEventDTO(
            id = id.value,
            title = title,
            description = description,
            assignedTo = assignedTo,
            projectId = projectId,
            dueDate = dueDate?.toString(),
            createdAt = createdAt?.toString(),
            updatedAt = updatedAt?.toString()
        )
}