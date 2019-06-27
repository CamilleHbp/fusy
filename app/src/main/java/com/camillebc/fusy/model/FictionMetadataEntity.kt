package com.camillebc.fusy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.camillebc.fictionproviderapi.FictionMetadata

@Entity(tableName = "fiction")
data class FictionMetadataEntity(
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
    var userRatings: Int? = null
    ) {
    constructor(fictionMetadata: FictionMetadata) : this(
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
        fictionMetadata.userRatings
    )
}
