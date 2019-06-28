package com.camillebc.fusy.core.model

import androidx.room.*
import me.camillebc.fictionproviderapi.FictionMetadata

@Entity(
    tableName = "fictions",
    indices = [Index("category_id")],
    foreignKeys = [
        ForeignKey(
            entity = Fiction::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class Fiction(
    @PrimaryKey
    val id: String,
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
    @ColumnInfo(name = "category_id")
    var categoryId: Long? = null
) {
    constructor(fictionMetadata: FictionMetadata, favourite: Boolean = false, category: Long? = null) : this(
        fictionMetadata.name,
        fictionMetadata.provider,
        fictionMetadata.fictionId,
        fictionMetadata.author,
        fictionMetadata.authorId,
        fictionMetadata.description.joinToString(),
        false,
        fictionMetadata.imageUrl,
        fictionMetadata.pageCount,
        fictionMetadata.ratings,
        fictionMetadata.userRatings,
        null
    )
}
