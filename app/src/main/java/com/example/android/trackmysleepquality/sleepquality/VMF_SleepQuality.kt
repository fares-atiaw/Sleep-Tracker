package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepDatabaseDao

class VMF_SleepQuality(private val key: Long, private val dataSource: SleepDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VM_SleepQuality::class.java)) {
            return VM_SleepQuality(key, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}