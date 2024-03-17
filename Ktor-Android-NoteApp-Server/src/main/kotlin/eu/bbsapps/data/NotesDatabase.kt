package eu.bbsapps.data

import eu.bbsapps.data.model.Note
import eu.bbsapps.data.model.User
import eu.bbsapps.util.checkHashForPassword
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class NotesDatabase : NoteDataAccessObject  {

    private val client = KMongo.createClient().coroutine
    private val database  = client.getDatabase("NoteDatabase")
    private val users = database.getCollection<User>()
    private val notes = database.getCollection<Note>()

    override suspend fun checkPasswordForEmail(email: String, password: String): Boolean {
        val actualPassword = users.findOne(User:: email eq email)?.password ?: return false
        return checkHashForPassword(password, actualPassword)
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        return users.findOne(User::email eq email) != null
    }

    override suspend fun registerUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}