package com.example.todolist

import android.util.Log

class InMemoryStorageProvider: StorageProvider {
    private var todoMap: MutableMap<Int, String> = mutableMapOf()
    override fun addEntry(content: String): Int {
        var leastIdUsed = 0
        for (i in todoMap) {
            if (i.key > leastIdUsed)
                leastIdUsed = i.key
        }
        leastIdUsed++
        todoMap[leastIdUsed] = content
        Log.i("storage", "added entry")
        return leastIdUsed
    }

    override fun deleteEntry(id: Int) {
        todoMap.remove(id)
    }

    override fun getEntries(): Map<Int, String> {
        return todoMap
    }
}