package eu.bbsapps.data

import eu.bbsapps.data.model.Note
import eu.bbsapps.data.model.User
import eu.bbsapps.util.checkHashForPassword
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.updateOne
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

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

    override suspend fun getUserIdWithEmail(email: String): String {
        return users.findOne(User::email eq email)?.id ?: ""
    }

    override suspend fun getAllNotesOfUser(userId: String): List<Note> {
        return notes.find(Note::owner eq userId).toList()
    }

    override suspend fun insertNote(note: Note): Boolean {
        return notes.insertOne(note).wasAcknowledged()
    }

    override suspend fun updateNote(note: Note): Boolean {
        notes.updateOne(note.id, setValue(Note::title, note.title))
        notes.updateOne(note.id, setValue(Note::title, note.text))
        return notes.updateOneById(note.id, setValue(Note::timeStamp, System.currentTimeMillis())).wasAcknowledged()
    }

    override suspend fun checkIfNoteExist(noteId: String): Boolean {
        return notes.findOneById(noteId) != null
    }

    override suspend fun isNoteOwnedBy(noteId: String, userId: String): Boolean {
        return notes.findOneById(noteId)?.owner == userId
    }
}