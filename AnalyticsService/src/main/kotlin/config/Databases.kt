package ru.itmo.config

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.itmo.model.TaskEventsTable


/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 * */
fun Application.configureDatabases() {
    val url = environment.config.property("postgres.url").getString()
    log.info("Connecting to postgres database at $url")
    val user = environment.config.property("postgres.username").getString()
    val password = environment.config.property("postgres.password").getString()

    Database.connect(
        url = url,
        user = user,
        password = password,
        driver = "org.postgresql.Driver",
    )

    transaction {
        SchemaUtils.create(TaskEventsTable)
    }
}
