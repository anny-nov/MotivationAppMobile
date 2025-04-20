package com.example.motivationapp.ui.fragments

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.motivationapp.R
import com.example.motivationapp.data.models.Event
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.databinding.ItemHabitCardBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HabitListAdapter(
    private val onDoneClicked: (Habit) -> Unit
) : RecyclerView.Adapter<HabitListAdapter.HabitViewHolder>() {

    private var items: List<Pair<Habit, List<Event>>> = listOf()

    fun submitList(list: List<Pair<Habit, List<Event>>>) {
        items = list
        notifyDataSetChanged()
    }

    inner class HabitViewHolder(val binding: ItemHabitCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHabitCardBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val (habit, events) = items[position]
        holder.binding.apply {
            habitNameTextView.text = habit.name
            repeatTextView.text = getRepeatText(habit.repeat)

            // Отображение звездочек сложности
            starContainer.removeAllViews()
            val context = root.context
            repeat(3) { i ->
                val star = AppCompatImageView(context).apply {
                    setImageResource(
                        if (i < habit.difficulty)
                            R.drawable.ic_star_filled
                        else
                            R.drawable.ic_star_outline
                    )
                    layoutParams = ViewGroup.LayoutParams(48, 48)
                }
                starContainer.addView(star)
            }

            val doneCount = events.count {
                if (it.executionTime == null) {
                    Log.w("HabitListAdapter", "executionTime is null for event: $it (habitId=${habit.id})")
                    false
                } else {
                    isSamePeriod(it.executionTime, habit.repeat ?: "")
                }
            }

            progressTextView.text = "$doneCount / ${getMaxExecutions(habit.repeat)}"

            completeButton.isEnabled = doneCount < getMaxExecutions(habit.repeat)
            completeButton.setOnClickListener {
                onDoneClicked(habit)
            }
        }
    }

    override fun getItemCount() = items.size

    private fun isSamePeriod(date: Date, repeat: String): Boolean {
        val cal = Calendar.getInstance()
        val now = cal.time
        cal.time = date

        return when {
            repeat.contains("в день") -> DateUtils.isToday(date.time)

            repeat.contains("в неделю") -> {
                val nowWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
                val nowYear = Calendar.getInstance().get(Calendar.YEAR)
                val eventWeek = cal.get(Calendar.WEEK_OF_YEAR)
                val eventYear = cal.get(Calendar.YEAR)
                eventWeek == nowWeek && eventYear == nowYear
            }

            repeat.contains("в месяц") -> {
                val nowMonth = Calendar.getInstance().get(Calendar.MONTH)
                val nowYear = Calendar.getInstance().get(Calendar.YEAR)
                val eventMonth = cal.get(Calendar.MONTH)
                val eventYear = cal.get(Calendar.YEAR)
                eventMonth == nowMonth && eventYear == nowYear
            }

            else -> false
        }
    }

    private fun getMaxExecutions(repeat: String): Int = when (repeat.trim().lowercase()) {
        "1 раз в день" -> 1
        "2 раза в день" -> 2
        "1 раз в неделю" -> 1
        "2 раза в неделю" -> 2
        "3 раза в неделю" -> 3
        "1 раз в месяц" -> 1
        else -> {
            Log.w("HabitListAdapter", "Неизвестная частота: $repeat")
            1
        }
    }

    private fun getRepeatText(repeat: String): String {
        return repeat
    }
}
