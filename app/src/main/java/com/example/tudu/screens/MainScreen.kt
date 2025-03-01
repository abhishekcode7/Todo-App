package com.example.tudu.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tudu.roomDB.TodoData
import com.example.tudu.util.TodoHeader
import com.example.tudu.util.Utils
import com.example.tudu.viewModels.MainScreenVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("newTask")
                },
                containerColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add, contentDescription = "Add Todo",
                    tint = Color.White
                )
            }
        }
    ) {

        val mainScreenVM: MainScreenVM = viewModel()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp, 0.dp)
        ) {
            Header(mainScreenVM)
            TodoList(mainScreenVM)
        }
    }

}

@Composable
fun Header(mainScreenVM: MainScreenVM) {

    val btnText = listOf(
        TodoHeader.Today,
        TodoHeader.All,
        TodoHeader.Upcoming,
        TodoHeader.Overdue
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth(1f)
    ) {
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = "Todo List",
            fontWeight = FontWeight.SemiBold
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(1f)
        ) {

            (1..4).forEach { index ->
                Button(
                    {
                        mainScreenVM.buttonEnabled = index
                        mainScreenVM.updateSelectedFilter(btnText[index - 1])
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (mainScreenVM.buttonEnabled == index) Color.White else Color.Black,
                        containerColor = if (mainScreenVM.buttonEnabled == index) Color.Black else Color(
                            0xFFDDE0E5
                        )
                    ),
                    contentPadding = PaddingValues(16.dp, 8.dp)
                ) {
                    Text(
                        text = btnText[index - 1].name,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

        }
    }
}

@Composable
fun TodoList(mainScreenVM: MainScreenVM) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(mainScreenVM.filteredItems) { todoItem ->
            TodoItem(todoItem, mainScreenVM)
        }
    }
}

@Composable
fun TodoItem(todoData: TodoData, mainScreenVM: MainScreenVM) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp,12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todoData.isCompleted,
                onCheckedChange = {
                    mainScreenVM.todoCompleted(todoData.id)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black
                )
            )
            Column {
                Text(
                    text = todoData.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
                )
                val time = Utils.formatTime(
                    todoData.hour,
                    todoData.minute
                ) + ", " + Utils.formatDateFromMillis(todoData.date)
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                )
            }

        }
    }

}