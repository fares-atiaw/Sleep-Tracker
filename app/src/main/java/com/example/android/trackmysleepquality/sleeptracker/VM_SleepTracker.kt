package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 * database => for interacting with the database functions from the ViewModel not the UI controller.
 * application => to get into the resources
 */
class VM_SleepTracker(val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application)
{
    var currentNight = MutableLiveData<SleepNight?>()   //tonight

    val allNights = database.getAllNights()

    private var _go_to_quality = MutableLiveData<SleepNight>()
    val go_to_quality : LiveData<SleepNight>
        get() = _go_to_quality

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    private var _go_to_details = MutableLiveData<Long>()
    val go_to_details = _go_to_details
//        get() = _go_to_details

    //  val startVisibility = currentNight.value?.endtTimeMilli == null
    var startVisibility = Transformations.map(currentNight){it == null}
    val stopVisibility = Transformations.map(currentNight){it != null}
    val clearVisibility = Transformations.map(allNights){it.isNotEmpty()}


    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            currentNight.value = getTonightFromDatabase()
            Log.w("here", "${currentNight.value}")
        }
    }

    private suspend fun getTonightFromDatabase() : SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()

            if (night?.endtTimeMilli != night?.startTimeMilli) night = null        // ???? Error but I have solved it => [ null != has-data ]

            night
        }
    }

    fun startTracking(){
        viewModelScope.launch {     // it handles what happens here and in the activity/fragment
            withContext(Dispatchers.IO) {
                database.insert(SleepNight())
            }
            currentNight.value = database.getTonight()
        }
    }

    // In Kotlin, the return@label syntax is used for specifying which function among
    // several nested ones this statement returns from.
    // In this case, we are specifying to return from launch(),
    // not the lambda.
    fun stopTracking(){
        viewModelScope.launch {
            val oldNight = currentNight.value ?: return@launch
            oldNight.endtTimeMilli = System.currentTimeMillis()

            launch { _go_to_quality.value = oldNight }
            withContext(Dispatchers.IO) {
                database.update(oldNight)
            }
        }
    }

   fun clearData(){
       viewModelScope.launch {
           withContext(Dispatchers.IO) {
               database.clear()
           }
           currentNight.value = null
           _showSnackbarEvent.value = true
       }
   }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    fun sleepNightClicked(id : Long) {
        _go_to_details.value = id
    }

    fun went() {
        _go_to_details.value = null
    }

}

// val allNights_String = Transformations.map(allNights) { formatNights(it, application.resources) }