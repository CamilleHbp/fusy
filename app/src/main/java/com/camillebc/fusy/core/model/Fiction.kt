package com.camillebc.fusy.core.model

import android.content.res.Resources
import androidx.room.*
import com.camillebc.fusy.core.CATEGORY_DEFAULT
import me.camillebc.fictionproviderapi.FictionMetadata

@Entity(
    tableName = "fictions",
    indices = [Index("category_name")],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["name"],
            childColumns = ["category_name"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class Fiction(
    @ColumnInfo(name = "fiction_id")
    val fictionId: String,
    val name: String,
    val provider: String,
    val author: String = "unknown",
    @ColumnInfo(name = "author_id")
    val authorId: String? = null,
    var description: String,
    var favourite: Boolean = false,
    @ColumnInfo(name = "image_url")
    var imageUrl: String?,
    var pageCount: Long? = null,
    var ratings: Int? = null,
    var userRatings: Int? = null,
    @ColumnInfo(name = "category_name")
    var categoryName: String? = null
) {
    constructor(
        fictionMetadata: FictionMetadata,
        favourite: Boolean = false,
        category: String? = null
    ) : this(
        fictionMetadata.fictionId,
        fictionMetadata.name,
        fictionMetadata.provider,
        fictionMetadata.author,
        fictionMetadata.authorId,
        fictionMetadata.description.joinToString(),
        favourite,
        fictionMetadata.imageUrl,
        fictionMetadata.pageCount,
        fictionMetadata.ratings,
        fictionMetadata.userRatings,
        category
    )

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
