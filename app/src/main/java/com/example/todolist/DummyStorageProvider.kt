package com.example.todolist

import android.util.Log

class DummyStorageProvider: StorageProvider {
    override fun addEntry(content: String): Int {
        // Do nothing
        Log.w("dummy", "this shouldn't be called")
        return 0
    }

    override fun deleteEntry(id: Int) {
        // the same
    }

    override fun getEntries(): Map<Int, String> {
        return mutableMapOf()
    }
}