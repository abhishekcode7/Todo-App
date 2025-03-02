package com.example.tudu.viewModels

import android.text.format.DateUtils
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tudu.roomDB.TodoData
import com.example.tudu.util.TodoHeader
import com.example.tudu.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenVM : ViewModel() {

    var todoItems by mutableStateOf<List<TodoData>>(emptyList())
    var filteredItems by mutableStateOf<List<TodoData>>(emptyList())
    var selectedFilter by mutableStateOf(TodoHeader.Today)
    var buttonEnabled by mutableIntStateOf(1)

    init {
        viewModelScope.launch {
            fetchUpdatedRoomData()
        }
    }

    fun todoCompleted(id: Int) {
        viewModelScope.launch {
            Utils.db.todoDao().updateTodoById(id, true)
            delay(2000L)
            Utils.db.todoDao().deleteById(id)
        }
//        todoItems = todoItems.mapIndexed { i, item ->
//            if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
//        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            Utils.db.todoDao().deleteById(id)
        }
    }

    private suspend fun fetchUpdatedRoomData() {
        Utils.db.todoDao().getAllTodo().collect {
            todoItems = it
            updateSelectedFilter(selectedFilter)
        }
    }

    fun updateSelectedFilter(tag: TodoHeader) {
        selectedFilter = tag
        when (tag) {
            TodoHeader.All -> filteredItems = todoItems
            TodoHeader.Today -> filterItemsByToday()
            TodoHeader.Upcoming -> filterItemsByUpcoming()
            TodoHeader.Overdue -> filterItemsByOverdue()
        }
    }

    private fun filterItemsByToday() {
        filteredItems = todoItems.filter {
            DateUtils.isToday(it.date)
        }
    }

    private fun filterItemsByUpcoming() {
        filteredItems = todoItems.filter {
            !DateUtils.isToday(it.date) && it.date > System.currentTimeMillis()
        }
    }

    private fun filterItemsByOverdue() {
        filteredItems = todoItems.filter {
            !DateUtils.isToday(it.date) && it.date < System.currentTimeMillis()
        }
    }
}