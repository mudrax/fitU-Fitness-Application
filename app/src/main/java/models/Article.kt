package models


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,

    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?, // room cant interpret Source dataType and hence we made a class Converter where we made how room convert it to string and interpret it
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable