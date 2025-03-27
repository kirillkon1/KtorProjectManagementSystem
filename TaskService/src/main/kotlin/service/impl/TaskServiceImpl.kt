package ru.itmo.service.impl

import ru.itmo.model.TaskDTO
import ru.itmo.repository.TaskRepository
import ru.itmo.service.TaskService

class TaskServiceImpl(private val taskRepository: TaskRepository ) : TaskService {
    override suspend fun createTask(taskDTO: TaskDTO): TaskDTO {
        return taskRepository.createTask(taskDTO)
    }

    override suspend fun getTaskById(id: Long): TaskDTO? {
        return taskRepository.getTaskById(id)
    }

    override suspend fun getAllTasks(): List<TaskDTO> {
        return taskRepository.getAllTasks()
    }

    override suspend fun updateTask(id: Long, taskDTO: TaskDTO): TaskDTO? {
        return taskRepository.updateTask(id, taskDTO)
    }

    override suspend fun deleteTask(id: Long): Boolean {
        return taskRepository.deleteTask(id)
    }
}