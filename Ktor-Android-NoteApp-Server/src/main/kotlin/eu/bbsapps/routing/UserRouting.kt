package eu.bbsapps.routing

import eu.bbsapps.data.requests.UserRegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {

    route("/register") {
        post {
            val userRegisterRequest = try {
                call.receive<UserRegisterRequest>()
            }catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}