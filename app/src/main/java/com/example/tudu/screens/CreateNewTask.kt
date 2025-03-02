package com.example.tudu.screens

import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tudu.roomDB.TodoData
import com.example.tudu.util.Utils
import com.example.tudu.util.Utils.formatDateFromMillis
import com.example.tudu.viewModels.NewTaskVM
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(navController: NavController, todoData: TodoData? = null) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Todo",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton({
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val newTaskVM: NewTaskVM = viewModel()

        val navigateBack by newTaskVM.navigateBack.collectAsState()

        LaunchedEffect(navigateBack) {
            if (navigateBack) {
                navController.popBackStack()
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TaskNameInput(newTaskVM)
            TaskDateInput(newTaskVM)
            TaskAlarm(newTaskVM)
            SaveTask(newTaskVM)
        }
    }
}

@Composable
fun TaskNameInput(newTaskVM: NewTaskVM) {
    Column(
        modifier = Modifier.padding(16.dp, 12.dp)
    ) {
        Text("Todo Title")
        OutlinedTextField(
            value = newTaskVM.taskTitle,
            onValueChange = { newTaskVM.updateTaskTitle(it) },
            placeholder = {
                Text("Enter your task")
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDateInput(newTaskVM: NewTaskVM) {

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, selectedHour, selectedMinute ->
            newTaskVM.selectedTime = Utils.formatTime(selectedHour, selectedMinute)
            newTaskVM.selectedHour = selectedHour
            newTaskVM.selectedMinute = selectedMinute
        },
        newTaskVM.hour,
        newTaskVM.minute,
        false // Set to 'true' for 24-hour format
    )

    // Show Date Picker
    if (newTaskVM.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { newTaskVM.showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    newTaskVM.showDatePicker = false
                    newTaskVM.datePickerState.selectedDateMillis?.let { millis ->
                        newTaskVM.selectedDate =
                            formatDateFromMillis(millis, "dd MMM yyyy") // Format date
                        newTaskVM.showTimePicker = true
                        newTaskVM.selectedDateMillis = millis
                        timePickerDialog.show()
                    }
                }) {
                    Text("Next")
                }
            }
        ) {
            DatePicker(state = newTaskVM.datePickerState)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp, 12.dp)
    ) {
        Text("Due By")
        OutlinedTextField(
            value = "${newTaskVM.selectedTime}, ${newTaskVM.selectedDate}",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            newTaskVM.showDatePicker = !newTaskVM.showDatePicker
                        }
                )
            }
        )
    }
}

@Composable
fun TaskAlarm(newTaskVM: NewTaskVM) {
    Row(
        modifier = Modifier
            .padding(16.dp, 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Set Alarm",
            style = MaterialTheme.typography.titleMedium
        )
        Switch(
            checked = newTaskVM.isChecked,
            onCheckedChange = { newTaskVM.isChecked = it }
        )
    }
}

@Composable
fun SaveTask(newTaskVM: NewTaskVM) {
    val context = LocalContext.current

    Button(
        {
            if (newTaskVM.taskTitle != "") {
                newTaskVM.saveTodo()
            } else {
                Toast.makeText(
                    context,
                    "Please add task title",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = Modifier
            .padding(16.dp, 12.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            "Save Todo",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}