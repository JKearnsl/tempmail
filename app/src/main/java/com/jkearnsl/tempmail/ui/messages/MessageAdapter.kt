package com.jkearnsl.tempmail.ui.messages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import android.widget.ArrayAdapter
import com.jkearnsl.tempmail.R

class MessageAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val messages: List<Message>) :
    ArrayAdapter<Message>(context, layoutResource, messages) {

    @NonNull
    override fun getView(position: Int, convertView: View?, @NonNull parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

        val message = messages[position]

        val avatarImageView = view.findViewById<ImageView>(R.id.avatar)
        val subjectTextView = view.findViewById<TextView>(R.id.subject)
        val emailTextView = view.findViewById<TextView>(R.id.email)
        val dateTextView = view.findViewById<TextView>(R.id.date)

        avatarImageView.setImageDrawable(ContextCompat.getDrawable(context, message.avatar))
        subjectTextView.text = message.subject
        emailTextView.text = message.email
        dateTextView.text = message.date

        return view
    }
}
