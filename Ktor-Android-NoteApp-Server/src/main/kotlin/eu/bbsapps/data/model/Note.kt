package eu.bbsapps.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    val title: String,
    val text: String,
    val timeStamp: Long = System.currentTimeMillis(),
    val owner: String = "",
    @BsonId
    val id: String = ObjectId().toString()
)
