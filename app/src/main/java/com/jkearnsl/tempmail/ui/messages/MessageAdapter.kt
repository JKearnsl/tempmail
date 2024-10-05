package com.jkearnsl.tempmail.ui.messages

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.jkearnsl.tempmail.R
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MessageAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val messages: List<Message>) :
    ArrayAdapter<Message>(context, layoutResource, messages) {

    @NonNull
    override fun getView(position: Int, convertView: View?, @NonNull parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

        val message = messages[position]

        val avatarTextView = view.findViewById<TextView>(R.id.avatar)
        val subjectTextView = view.findViewById<TextView>(R.id.subject)
        val emailTextView = view.findViewById<TextView>(R.id.email)
        val dateTextView = view.findViewById<TextView>(R.id.date)

        avatarTextView.text = message.email.first().toString().uppercase()

        val color = generateColorFromEmail(message.email)
        val drawable = ContextCompat.getDrawable(context, R.drawable.circle_background) as GradientDrawable
        drawable.setColor(color)
        avatarTextView.background = drawable

        subjectTextView.text = message.subject
        emailTextView.text = message.email
        dateTextView.text = formatDate(message.date)

        return view
    }

    private fun generateColorFromEmail(email: String): Int {
        val hash = email.hashCode()
        val r = (hash and 0xFF0000) shr 16
        val g = (hash and 0x00FF00) shr 8
        val b = (hash and 0x0000FF)
        return Color.rgb(r, g, b)
    }

    private fun formatDate(date: Date): String {
        val now = Calendar.getInstance().time
        val diff = now.time - date.time

        return when {
            TimeUnit.MILLISECONDS.toDays(diff) < 1 -> {
                val hours = date.hours.toString().padStart(2, '0')
                val minutes = date.minutes.toString().padStart(2, '0')

                when {
                    TimeUnit.MILLISECONDS.toMinutes(diff) < 1 -> {
                        if (Locale.getDefault().language == "ru") {
                            "Только что"
                        } else {
                            "Just now"
                        }
                    }
                    else -> {
                        "$hours:$minutes"
                    }
                }
            }
            date.month == now.month && date.year == now.year -> {
                val outputFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
                outputFormat.format(date)
            }
            else -> {
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                outputFormat.format(date)
            }
        }
    }
}