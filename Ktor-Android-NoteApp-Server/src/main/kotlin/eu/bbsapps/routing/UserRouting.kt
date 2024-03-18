package eu.bbsapps.routing

import eu.bbsapps.data.model.User
import eu.bbsapps.data.requests.UserLoginRequest
import eu.bbsapps.data.requests.UserRegisterRequest
import eu.bbsapps.database
import eu.bbsapps.util.getHashWithSalt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {

    route("/login") {
        post {

            val userLoginRequest = try {
                call.receive<UserLoginRequest>()
            }catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (database.checkPasswordForEmail(userLoginRequest.email,userLoginRequest.password)) {
                call.respond(HttpStatusCode.OK)
                return@post
            }

            call.respond(HttpStatusCode.Unauthorized)

        }
    }

    route("/register") {
        post {
            val userRegisterRequest = try {
                call.receive<UserRegisterRequest>()
            }catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if(database.checkIfEmailExist(userRegisterRequest.email)) {
                call.respond(HttpStatusCode.Conflict)
            }

            val emailPattern = ("^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$").toRegex()

            if(!emailPattern.matches(userRegisterRequest.email)) {
                call.respond(HttpStatusCode.BadRequest)
            }

            val hashPassword = getHashWithSalt(userRegisterRequest.password)

            if(database.registerUser(User(userRegisterRequest.email , userRegisterRequest.username, hashPassword))) {
                call.respond(HttpStatusCode.Created)
            }else{
                call.respond(HttpStatusCode.InternalServerError)
            }

        }
    }
}