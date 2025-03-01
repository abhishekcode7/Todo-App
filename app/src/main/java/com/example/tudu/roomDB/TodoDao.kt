package com.example.tudu.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoData: TodoData)

    @Query("SELECT * FROM todo_table")
    fun getAllTodo(): Flow<List<TodoData>>

    @Query("DELETE FROM todo_table WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

    @Query("UPDATE todo_table SET id = :id, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTodoById(id: Int, isCompleted:Boolean)
}