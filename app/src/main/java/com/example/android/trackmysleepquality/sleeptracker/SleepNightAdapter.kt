package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ItemSleepDetailsBinding

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SleepNightAdapter(val clickListener: SleepNightListener): ListAdapter<SleepNight, SleepNightAdapter.MyViewHolder>(Diff_SleepNight()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.fromInflating(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class MyViewHolder private constructor(private val binding: ItemSleepDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SleepNight, clickListener: SleepNightListener) { // use this instead of using transactions for each view here (Binding Adapter).
            binding.x = data
            binding.action = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun fromInflating(parent: ViewGroup): MyViewHolder {
                val binding: ItemSleepDetailsBinding = ItemSleepDetailsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MyViewHolder(binding)
            }
        }
    }

}

class Diff_SleepNight : DiffUtil.ItemCallback<SleepNight>()
{
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}

class SleepNightListener(val listener : (id :Long) -> Unit)
{
    fun onClick(night: SleepNight) {
        listener(night.nightId)
    }
}


/** (By using kotlin extensions) Binding Adapter which means adding attribute(s) to the View itself and write its related implementation.
   And in the XML, use the variable that related to the data-class to bind it with each view there [ @{x} ]**/

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(data :SleepNight?){
    data?.let {
        text = convertDurationToFormatted(data.startTimeMilli, data.endtTimeMilli, context.resources)
        if(data.sleepQuality <= 1 && data.sleepQuality != -1) setTextColor(Color.RED)
        else setTextColor(Color.BLACK)
    }
}
@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(data :SleepNight?){
    data?.let {
        text = convertNumericQualityToString(data.sleepQuality, context.resources)
        if(data.sleepQuality <= 1 && data.sleepQuality != -1) setTextColor(Color.RED)
        else setTextColor(Color.BLACK)
    }
}
@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(data :SleepNight?){
    data?.let {
        setImageResource(
            when (data.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_launcher_sleep_tracker_background
            })
    }
}


//
//sealed class DataItem {
//    data class SleepNightItem(val sleepNight: SleepNight): DataItem() {
//        override val id = sleepNight.nightId
//    }
//
//    object Header: DataItem() {
//        override val id = Long.MIN_VALUE
//    }
//
//    abstract val id: Long
//}