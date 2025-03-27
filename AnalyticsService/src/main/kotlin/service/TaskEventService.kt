package ru.itmo.service

import ru.itmo.model.TaskEventDTO

interface TaskEventService {
    suspend fun createTaskEvent(taskEventDTO: TaskEventDTO): TaskEventDTO

    suspend fun getTaskEventById(id: Long): TaskEventDTO?

    suspend fun getAllTaskEvents(): List<TaskEventDTO>

    suspend fun createRequest(id: Long)
}