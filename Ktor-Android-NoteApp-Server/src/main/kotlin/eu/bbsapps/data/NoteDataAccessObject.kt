package eu.bbsapps.data

import eu.bbsapps.data.model.User

interface NoteDataAccessObject {

    suspend fun checkPasswordForEmail(email: String, password: String) : Boolean

    suspend fun checkIfEmailExist(email: String) : Boolean

    suspend fun registerUser(user: User) : Boolean
}