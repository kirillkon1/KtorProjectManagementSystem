package ru.itmo.config

import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.koin.ktor.ext.inject
import ru.itmo.model.TaskEventDTO
import ru.itmo.repository.TaskEventRepository
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


fun Application.configureKafka() {


    val repo: TaskEventRepository by inject<TaskEventRepository>()
    val jsonParser = Json { ignoreUnknownKeys = true }
    val kafkaConfig = kafkaConfig()

    // Kafka Consumer для получения ответов из TaskService (топик "task-response")
    val consumerProps = Properties().apply {
        put("bootstrap.servers", kafkaConfig.url)
        put("group.id", "analytics-service-group")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    }
    val consumer = KafkaConsumer<String, String>(consumerProps)
    consumer.subscribe(listOf("task-response"))

    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            for (record in records) {
                val taskJson = record.value()
                println("AnalyticsService received task response: $taskJson")
                try {
                    val task: TaskEventDTO = jsonParser.decodeFromString<TaskEventDTO>(taskJson)
                    repo.createTask(task)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}