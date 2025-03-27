package ru.itmo.apigateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LogFilter : AbstractGatewayFilterFactory<LogFilter.Config>(Config::class.java) {
    private val logger = LoggerFactory.getLogger(LogFilter::class.java)

    data class Config(
        val baseMessage: String = "Default Message",
    )

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            logger.info("API Gateway: Request URI: ${exchange.request.uri}")
            chain.filter(exchange).then(
                Mono.fromRunnable {
                    logger.info("API Gateway: Response Status: ${exchange.response.statusCode}")
                },
            )
        }
    }
}
