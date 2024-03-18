package eu.bbsapps.routing

import eu.bbsapps.data.model.Note
import eu.bbsapps.data.requests.NoteRequest
import eu.bbsapps.database
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes() {
    route("/notes") {
            authenticate {
                get {
                    val email = call.principal<UserIdPrincipal>()!!.name
                    call.respond(HttpStatusCode.OK, database.getAllNotesOfUser(database.getUserIdWithEmail(email)))
                }

                post {
                    val email = call.principal<UserIdPrincipal>()!!.name

                    val noteData = try {
                        call.receive<NoteRequest>()
                    }catch (e: ContentTransformationException) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    val note = Note(
                        title = noteData.title,
                        text = noteData.title,
                        timeStamp = System.currentTimeMillis(),
                        owner = database.getUserIdWithEmail(email)
                    )

                    database.insertNote(note)

                    call.respond(HttpStatusCode.OK, note)
                }
                patch {
                    val email = call.principal<UserIdPrincipal>()!!.name

                    val noteData = try {
                        call.receive<NoteRequest>()
                    }catch (e: ContentTransformationException) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@patch
                    }

                    val noteId = call.request.queryParameters["id"] ?: ""
                    if (noteId == "") {
                        call.respond(HttpStatusCode.BadRequest)
                        return@patch
                    }

                    if (!database.checkIfEmailExist(noteId)) {
                        call.respond(HttpStatusCode.NotFound)
                        return@patch
                    }

                    if (database.isNoteOwnedBy(noteId, database.getUserIdWithEmail(email))) {
                        val note = Note(
                            title = noteData.title,
                            text = noteData.text,
                            id = noteId
                        )

                        database.updateNote(note)
                        call.respond(HttpStatusCode.OK, note)
                        return@patch
                    }else {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                }
            }
    }
}