package com.example.android.trackmysleepquality.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(

    @PrimaryKey(autoGenerate = true)
    var nightId : Long = 0L,
    @ColumnInfo(name = "start_time_milli")           // refers to the name in the database
    var startTimeMilli : Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time_milli")            // refers to the name in the database
    var endtTimeMilli : Long = startTimeMilli,
    @ColumnInfo(name = "quality_rating")            // refers to the name in the database
    var sleepQuality : Int = -1
)
    :Parcelable