package eu.bbsapps.data

import eu.bbsapps.data.model.Note
import eu.bbsapps.data.model.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class NotesDatabase : NoteDataAccessObject  {

    private val client = KMongo.createClient().coroutine
    private val database  = client.getDatabase("NoteDatabase")
    private val user = database.getCollection<User>()
    private val note = database.getCollection<Note>()

    override suspend fun checkPasswordForEmail(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(user: User): Boolean {
        TODO("Not yet implemented")
    }
}