package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface CategoryDao {
    @Query("select * from categories")
    suspend fun getAllCategories(): List<Category>?

    @Query("select * from categories where id = :id limit 1")
    suspend fun getCategory(id: Long): Category?

    @Query("select * from categories where name = :name limit 1")
    suspend fun getCategoryByName(name: String?): Category

    @Insert(onConflict = REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Update(onConflict = REPLACE)
    suspend fun updateCategory(category: Category)

    @Update(onConflict = REPLACE)
    suspend fun updateCategories(categories: List<Category>)

    @Delete
    suspend fun deleteCategory(category: Category)
}