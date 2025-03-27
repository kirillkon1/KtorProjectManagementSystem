package ru.itmo.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.itmo.model.TaskDTO
import ru.itmo.model.TaskEntity
import java.time.LocalDateTime

class TaskRepository {

    suspend fun createTask(taskDTO: TaskDTO): TaskDTO = newSuspendedTransaction(Dispatchers.IO) {
        val taskEntity = TaskEntity.new {
            title = taskDTO.title
            description = taskDTO.description
            status = taskDTO.status
            priority = taskDTO.priority
            assignedTo = taskDTO.assignedTo
            projectId = taskDTO.projectId
            dueDate = taskDTO.dueDate?.let { java.time.LocalDateTime.parse(it) }
            // createdAt задаётся по умолчанию в таблице
        }
        taskEntity.toDTO()
    }

    suspend fun getTaskById(id: Long): TaskDTO? = newSuspendedTransaction(Dispatchers.IO) {
        TaskEntity.findById(id)?.toDTO()
    }

    suspend fun getAllTasks(): List<TaskDTO> = newSuspendedTransaction(Dispatchers.IO) {
        TaskEntity.all().map { it.toDTO() }
    }

    suspend fun updateTask(id: Long, taskDTO: TaskDTO): TaskDTO? = newSuspendedTransaction(Dispatchers.IO) {
        val taskEntity = TaskEntity.findById(id) ?: return@newSuspendedTransaction null

        taskEntity.title = taskDTO.title
        taskEntity.description = taskDTO.description
        taskEntity.status = taskDTO.status
        taskEntity.priority = taskDTO.priority
        taskEntity.assignedTo = taskDTO.assignedTo
        taskEntity.projectId = taskDTO.projectId
        taskEntity.dueDate = taskDTO.dueDate?.let { java.time.LocalDateTime.parse(it) }
        taskEntity.updatedAt = LocalDateTime.now()

        taskEntity.toDTO()
    }

    suspend fun deleteTask(id: Long): Boolean = newSuspendedTransaction(Dispatchers.IO) {
        val taskEntity = TaskEntity.findById(id)
        if (taskEntity != null) {
            taskEntity.delete()
            true
        } else {
            false
        }
    }
}