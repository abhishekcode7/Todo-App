package com.example.tudu.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(modifier: Modifier = Modifier,navController: NavController) {
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp, 0.dp)
        ) {
            Header()
        }
    }

}

@Composable
fun Header(modifier: Modifier = Modifier) {

    val buttonEnabled = remember { mutableIntStateOf(1) }
    val btnText = listOf("All", "Today", "Upcoming", "Overdue")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth(1f)
    ) {
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = "Task List",
            fontWeight = FontWeight.SemiBold
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(1f)
        ) {

            (1..4).forEach { index ->
                Button(
                    {
                        buttonEnabled.intValue = index
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (buttonEnabled.intValue == index) Color.White else Color.Black,
                        containerColor = if (buttonEnabled.intValue == index) Color.Black else Color(
                            0xFFDDE0E5
                        )
                    ),
                    contentPadding = PaddingValues(16.dp, 8.dp)
                ) {
                    Text(text = btnText[index - 1], style = MaterialTheme.typography.titleSmall)
                }
            }

        }
    }
}