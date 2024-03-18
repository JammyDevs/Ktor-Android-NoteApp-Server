package eu.bbsapps.data

import eu.bbsapps.data.model.Note
import eu.bbsapps.data.model.User

interface NoteDataAccessObject {

    suspend fun checkPasswordForEmail(email: String, password: String) : Boolean

    suspend fun checkIfEmailExist(email: String) : Boolean

    suspend fun registerUser(user: User) : Boolean

    suspend fun getUserIdWithEmail(email: String): String

    suspend fun getAllNotesOfUser(userId: String): List<Note>

    suspend fun insertNote(note: Note): Boolean

    suspend fun updateNote(note: Note): Boolean

    suspend fun checkIfNoteExist(noteId: String): Boolean

    suspend fun isNoteOwnedBy(noteId: String, userId: String): Boolean
}