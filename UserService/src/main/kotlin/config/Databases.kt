@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.config

import io.ktor.server.application.*
import model.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
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
        SchemaUtils.create(UsersTable)
    }
}
