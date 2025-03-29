package ru.itmo.config


import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.ktor.ext.inject
import ru.itmo.repository.TaskRepository
import java.time.Duration
import java.util.*


data class KafkaConfig(
    val url: String,
)


fun Application.kafkaConfig(): KafkaConfig {
    val config = environment.config
    return KafkaConfig(
        url = config.property("kafka.url").getString(),
    )
}


fun Application.kafkaConfigure(){

    val kafkaConfig = kafkaConfig()

    val taskRepository: TaskRepository by inject<TaskRepository>()

    // Инициализация Kafka Consumer для топика "task-requests"
    val consumerProps = Properties().apply {
        put("bootstrap.servers", kafkaConfig.url)
        put("group.id", "task-service-group")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    }
    val consumer = KafkaConsumer<String, String>(consumerProps)
    consumer.subscribe(listOf("task-requests"))

    // Инициализация Kafka Producer для топика "task-response"
    val producerProps = Properties().apply {
        put("bootstrap.servers", "kafka:9092")
        put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }
    val producer = KafkaProducer<String, String>(producerProps)

    // Запускаем отдельный поток для прослушивания запросов
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            for (record in records) {
                val taskIdStr = record.value()
                println("TaskService received request for taskId = $taskIdStr")
                val task = taskRepository.getTaskById(taskIdStr.toLong())
                if (task != null) {
                    val taskJson = Json.encodeToString(task)
                    producer.send(ProducerRecord("task-response", taskIdStr, taskJson))
                    println("TaskService sent response for taskId = $taskIdStr")
                } else {
                    println("TaskService did not find task with id = $taskIdStr")
                }
            }
        }
    }
}