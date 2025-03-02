package com.example.tudu.roomDB

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo_table")
data class TodoData(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val title:String,
    val date:Long,
    val hour:Int,
    val minute:Int,
    var isCompleted:Boolean = false
) : Serializable
