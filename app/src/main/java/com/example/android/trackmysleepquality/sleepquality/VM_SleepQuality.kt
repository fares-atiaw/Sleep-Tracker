package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VM_SleepQuality(val key :Long , val database: SleepDatabaseDao) : ViewModel() {

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tonight = database.get(key) ?: return@launch
            tonight.sleepQuality = quality
            database.update(tonight)
        }
        _navigateToSleepTracker.value = true
    }

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

}