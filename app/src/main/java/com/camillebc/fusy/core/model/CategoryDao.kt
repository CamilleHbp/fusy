package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface CategoryDao {
    @Query("select * from categories")
    fun getAllCategories(): List<Category>

    @Query("select * from categories where id = :id limit 1")
    fun getCategory(id: Long): Category

    @Insert(onConflict = REPLACE)
    fun insertCategory(category: Category)

    @Insert(onConflict = REPLACE)
    fun insertCategories(categories: List<Category>)

    @Update(onConflict = REPLACE)
    fun updateCategory(category: Category)

    @Update(onConflict = REPLACE)
    fun updateCategories(categories: List<Category>)

    @Delete
    fun deleteCategory(category: Category)
}