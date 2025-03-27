package ru.itmo.config

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.koin.dsl.module
import ru.itmo.repository.TaskEventRepository
import ru.itmo.service.TaskEventService
import ru.itmo.service.impl.TaskEventServiceImpl
import java.util.*


val appModule = module {
    // Регистрируем TaskEventRepository
    single { TaskEventRepository() }

    // Регистрируем KafkaProducer<String, String>
    single<KafkaProducer<String, String>> {
        val props = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        }
        KafkaProducer<String, String>(props)
    }

    // Регистрируем TaskEventService с обоими зависимостями
    single<TaskEventService> { TaskEventServiceImpl(get(), get()) }
}