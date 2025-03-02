package com.example.tudu.viewModels

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tudu.roomDB.TodoData
import com.example.tudu.util.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.min


class NewTaskVM : ViewModel() {

    var taskTitle by mutableStateOf("")
        private set
    private var taskId by mutableIntStateOf(1)
    private val calendar: Calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    var showDatePicker by mutableStateOf(false)
    var showTimePicker by mutableStateOf(false)
    var selectedDate by mutableStateOf(Utils.formatDateFromMillis(System.currentTimeMillis()))
    var selectedTime by mutableStateOf(Utils.formatTime(hour, minute))
    var selectedHour by mutableStateOf(hour)
    var selectedMinute by mutableStateOf(minute)
    var selectedDateMillis by mutableStateOf(System.currentTimeMillis())

    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerState = DatePickerState(Locale.getDefault())

    var isChecked by mutableStateOf(false)
    var isEditing = false

    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack

    fun updateTaskTitle(title: String) {
        taskTitle = title
    }

    fun setInitialData(todoData: TodoData?) {
        todoData?.let {
            isEditing = true

            taskId = it.id
            taskTitle = it.title
            selectedHour = it.hour
            selectedMinute = it.minute
            isChecked = it.isCompleted
            selectedDateMillis = it.date

            selectedDate = Utils.formatDateFromMillis(it.date)
            selectedTime = Utils.formatTime(it.hour, it.minute)
        }
    }

    fun saveTodo() {
        if (isEditing) {
            updateTodo()
        } else createAndSaveTodo()
    }

    private fun updateTodo() {
        viewModelScope.launch {
            Utils.db.todoDao().updateAllTodoFields(
                id = taskId,
                title = taskTitle,
                date = selectedDateMillis,
                hour = selectedHour,
                minute = selectedMinute,
            )
            _navigateBack.value = true
        }
    }

    private fun createAndSaveTodo() {
        val todoData = TodoData(
            title = taskTitle,
            date = selectedDateMillis,
            hour = selectedHour,
            minute = selectedMinute,
        )
        viewModelScope.launch {
            Utils.db.todoDao().insertTodo(todoData)
            _navigateBack.value = true
        }
    }
}