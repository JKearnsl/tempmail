package com.jkearnsl.tempmail.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jkearnsl.tempmail.R
import com.jkearnsl.tempmail.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView = binding.messagesListView
        val messages = mutableListOf(
            Message(R.drawable.ic_menu_slideshow, "Subject 1", "Email content 1", "Date 1"),
            Message(R.drawable.ic_launcher_background, "Subject 2", "Email content 2", "Date 2"),
        )
        val adapter = MessageAdapter(requireContext(), R.layout.list_item_message, messages)
        listView.adapter = adapter

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            val refreshedMessages = listOf(
                Message(R.drawable.ic_menu_camera, "Refreshed Subject 1", "Refreshed Email content 1", "Refreshed Date 1"),
                Message(R.drawable.ic_menu_gallery, "Refreshed Subject 2", "Refreshed Email content 2", "Refreshed Date 2"),
            )
            messages.clear()
            messages.addAll(refreshedMessages)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}