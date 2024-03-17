package eu.bbsapps

import eu.bbsapps.data.NoteDataAccessObject
import eu.bbsapps.data.NotesDatabase
import eu.bbsapps.routing.userRoutes
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val database: NoteDataAccessObject = NotesDatabase()

fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging) {
        level = Level.INFO
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Authentication) {

    }

    install(Routing) {
        route("/") {
            get {
                call.respond("Hello World!")
            }

            userRoutes()
        }


    }
}
