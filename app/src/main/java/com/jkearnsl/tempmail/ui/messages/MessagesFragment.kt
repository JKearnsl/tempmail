package com.jkearnsl.tempmail.ui.messages

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jkearnsl.tempmail.R
import com.jkearnsl.tempmail.TempMail
import com.jkearnsl.tempmail.databinding.FragmentMessagesBinding
import kotlinx.coroutines.launch

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private lateinit var refreshRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView = binding.messagesListView
        val app = requireActivity().application as TempMail

        val adapter = MessageAdapter(
            requireContext(),
            R.layout.list_item_message,
            app.core.messages
        )
        listView.adapter = adapter

        handler = Handler(Looper.getMainLooper())
        refreshRunnable = object : Runnable {
            override fun run() {
                lifecycleScope.launch {
                    app.core.refreshMessages()
                    adapter.notifyDataSetChanged()
                }
                handler.postDelayed(this, 5000)
            }
        }

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                app.core.refreshMessages()
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
        }


        return root
    }

    override fun onResume() {
        super.onResume()
        handler.post(refreshRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}