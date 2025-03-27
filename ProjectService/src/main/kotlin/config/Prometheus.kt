@file:Suppress("ktlint:standard:no-wildcard-imports")

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun Application.configurePrometheus() {
    // Создаем реестр Prometheus
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    // Устанавливаем плагин MicrometerMetrics с нашим реестром
    install(MicrometerMetrics) {
        registry = prometheusRegistry
        // При необходимости можно настроить фильтры, метки и пр.
    }

    // Экспорт метрик по эндпоинту /metrics
    routing {
        get("/metrics") {
            call.respondText(prometheusRegistry.scrape(), ContentType.Text.Plain)
        }
    }
}
