package ru.itmo.service.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import ru.itmo.model.TaskEventDTO
import ru.itmo.repository.TaskEventRepository
import ru.itmo.service.TaskEventService

class TaskEventServiceImpl(
    private val taskEventRepository: TaskEventRepository,
    private val producer: KafkaProducer<String, String>
) : TaskEventService {

    override suspend fun createTaskEvent(taskEventDTO: TaskEventDTO): TaskEventDTO
    = taskEventRepository.createTask(taskEventDTO)

    override suspend fun getTaskEventById(id: Long): TaskEventDTO? =
        taskEventRepository.getTaskById(id)

    override suspend fun getAllTaskEvents(): List<TaskEventDTO> =
        taskEventRepository.getAllTasks()

    override suspend fun createRequest(id: Long) {
        withContext(Dispatchers.IO) {
            // Формируем сообщение, где ключ и значение — строковое представление id
            val record = ProducerRecord<String, String>("task-requests", id.toString(), id.toString())
            // Отправляем сообщение и ожидаем подтверждения отправки
            producer.send(record).get()
        }
        println("TaskEventService: sent request for task id $id")
    }


}