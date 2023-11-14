package com.example.todolist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AppState {
    var storageProvider: StorageProvider = DummyStorageProvider()
    var dialogId: Int = 0
    var idDelete: Int = 0
    fun closeDialog(): AppState {
        val newState = AppState()
        newState.storageProvider = storageProvider
        return newState
    }

    fun openNewTodo(): AppState {
        val newState = AppState()
        newState.storageProvider = storageProvider
        newState.dialogId = 1
        return newState
    }

    fun deleteTodo(id: Int): AppState {
        val newState = AppState()
        newState.storageProvider = storageProvider
        newState.dialogId = 2
        newState.idDelete = id
        return newState
    }

    companion object {
        @JvmStatic
        private var mutableAppState: MutableState<AppState>? = null

        @JvmStatic
        fun getState(): MutableState<AppState> {
            if (mutableAppState == null) {
                mutableAppState = mutableStateOf(AppState())
                mutableAppState!!.value.storageProvider = InMemoryStorageProvider()
            }
            return mutableAppState as MutableState<AppState>
        }
    }
}