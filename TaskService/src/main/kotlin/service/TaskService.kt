package ru.itmo.service

import ru.itmo.model.TaskDTO

interface TaskService {

    suspend fun createTask(taskDTO: TaskDTO): TaskDTO

    suspend fun getTaskById(id: Long): TaskDTO?

    suspend fun getAllTasks(): List<TaskDTO>

    suspend fun updateTask(id: Long, taskDTO: TaskDTO): TaskDTO?

    suspend fun deleteTask(id: Long): Boolean
}