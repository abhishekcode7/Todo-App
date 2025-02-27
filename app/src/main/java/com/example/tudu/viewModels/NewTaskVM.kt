package com.example.tudu.viewModels

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tudu.util.Utils
import java.util.Calendar
import java.util.Locale


class NewTaskVM: ViewModel() {

    var taskTitle by mutableStateOf("")
        private set

    private val calendar: Calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    
    var showDatePicker by mutableStateOf(false)
    var showTimePicker by mutableStateOf(false)
    var selectedDate by mutableStateOf(Utils.formatDateFromMillis(System.currentTimeMillis()))
    var selectedTime by mutableStateOf(Utils.formatTime(hour, minute))
    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerState = DatePickerState(Locale.getDefault())

    var isChecked by mutableStateOf(false)
    
    fun updateTaskTitle(title:String){
        taskTitle = title
    }
}