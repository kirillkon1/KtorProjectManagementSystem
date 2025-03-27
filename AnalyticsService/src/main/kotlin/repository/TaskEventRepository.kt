package ru.itmo.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.itmo.model.TaskEventEntity
import ru.itmo.model.TaskEventDTO
import java.time.LocalDateTime

class TaskEventRepository {

    suspend fun createTask(TaskEventDTO: TaskEventDTO): TaskEventDTO = newSuspendedTransaction(Dispatchers.IO) {
        val taskEventEntity = TaskEventEntity.new {
            title = TaskEventDTO.title + "-${(Math.random() * 100000).toLong()}"
            description = TaskEventDTO.description + "/w ${(Math.random() * 100000).toLong()}"
            assignedTo = TaskEventDTO.assignedTo
            projectId = TaskEventDTO.projectId
            dueDate = TaskEventDTO.dueDate?.let { java.time.LocalDateTime.parse(it) }
        }
        taskEventEntity.toDTO()
    }

    suspend fun getTaskById(id: Long): TaskEventDTO? = newSuspendedTransaction(Dispatchers.IO) {
        TaskEventEntity.findById(id)?.toDTO()
    }

    suspend fun getAllTasks(): List<TaskEventDTO> = newSuspendedTransaction(Dispatchers.IO) {
        TaskEventEntity.all().map { it.toDTO() }
    }

    suspend fun updateTask(id: Long, taskEventDTO: TaskEventDTO): TaskEventDTO? = newSuspendedTransaction(Dispatchers.IO) {
        val taskEventEntity = TaskEventEntity.findById(id) ?: return@newSuspendedTransaction null

        taskEventEntity.title = taskEventDTO.title
        taskEventEntity.description = taskEventDTO.description
        taskEventEntity.assignedTo = taskEventDTO.assignedTo
        taskEventEntity.projectId = taskEventDTO.projectId
        taskEventEntity.dueDate = taskEventDTO.dueDate?.let { java.time.LocalDateTime.parse(it) }
        taskEventEntity.updatedAt = LocalDateTime.now()

        taskEventEntity.toDTO()
    }

    suspend fun deleteTask(id: Long): Boolean = newSuspendedTransaction(Dispatchers.IO) {
        val taskEventEntity = TaskEventEntity.findById(id)
        if (taskEventEntity != null) {
            taskEventEntity.delete()
            true
        } else {
            false
        }
    }
    
    
}