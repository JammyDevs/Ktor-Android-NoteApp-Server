package eu.bbsapps

import eu.bbsapps.data.NoteDataAccessObject
import eu.bbsapps.data.NotesDatabase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
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

    install(Authentication) {

    }

    install(Routing) {
        route("/") {
            get {
                call.respond("Hello World!")
            }
        }


    }
}
