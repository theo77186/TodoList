package com.example.todolist

interface StorageProvider {
    fun addEntry(content: String): Int
    fun deleteEntry(id: Int)
    fun getEntries(): Map<Int, String>
}
