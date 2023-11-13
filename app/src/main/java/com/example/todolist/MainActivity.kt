package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appState = mutableStateOf(AppState())
        appState.value.storageProvider = InMemoryStorageProvider()
        setContent {
            TodoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldApp(appState)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ScaffoldApp(appState: MutableState<AppState> = mutableStateOf(AppState())) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo List") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { appState.value = appState.value.openNewTodo() }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding -> Column (modifier = Modifier.padding(innerPadding)) {
        TodoItemList(appState = appState)
        when (appState.value.dialogId) {
            1 -> AlertNewTodo(appState = appState)
            2 -> AlertDeleteTodo(appState = appState)
        }
    }
    }
}

@Composable
fun TodoItemList(appState: MutableState<AppState>) {
    val sp = appState.value.storageProvider
    val listTodo = sp.getEntries().toList()
    val sortedListTodo = listTodo.sortedBy { it.first }
    for (el in sortedListTodo) {
        TodoItem(appState, el.first, el.second)
    }
}

@Composable
fun TodoItem(appState: MutableState<AppState>, id: Int = 0, content: String = "") {
    Row(modifier = Modifier
        .clickable(onClick = {})
        .padding(24.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = content, modifier = Modifier.weight(1f))
        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.clickable(onClick = {
            appState.value = appState.value.deleteTodo(id)
        }))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertNewTodo(appState: MutableState<AppState>) {
    val todoValue = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { appState.value = appState.value.closeDialog() },
        confirmButton = {
            TextButton(onClick = {
                appState.value.storageProvider.addEntry(todoValue.value)
                appState.value = appState.value.closeDialog()
            }) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                appState.value = appState.value.closeDialog()
            }) {
                Text("Annuler")
            }
        },
        title = {
            Text("Nouvelle note")
        },
        text = {
            TextField(value = todoValue.value, onValueChange = { value: String -> todoValue.value = value })
        }
    )
}

@Composable
fun AlertDeleteTodo(appState: MutableState<AppState>) {
    val sp = appState.value.storageProvider
    AlertDialog(
        onDismissRequest = { appState.value.closeDialog() },
        confirmButton = {
            TextButton(onClick = {
                sp.deleteEntry(appState.value.idDelete)
                appState.value = appState.value.closeDialog()
            }) {
                Text("Oui")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                appState.value = appState.value.closeDialog()
            }) {
                Text("Non")
            }
        },
        text = {
            Text("Supprimer la note?")
        }
    )
}