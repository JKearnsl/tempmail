package com.jkearnsl.tempmail.ui.messages

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.jkearnsl.tempmail.R

class MessageAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val messages: List<MessageItem>) :
    ArrayAdapter<MessageItem>(context, layoutResource, messages) {

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

        view.setOnClickListener {
            val bundle = bundleOf("messageId" to message.id)
            val activity = context as FragmentActivity
            val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
            navHostFragment.navController.navigate(R.id.messageDetailFragment, bundle)
        }

        return view
    }
}