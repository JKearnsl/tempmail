package com.jkearnsl.tempmail.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jkearnsl.tempmail.databinding.FragmentMessageDetailBinding

class MessageDetailFragment : Fragment() {

    private var _binding: FragmentMessageDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val message = arguments?.getParcelable<Message>("message")
        binding.subjectTextView.text = "Subject goes here"
        binding.emailTextView.text = "Email goes here"
        binding.dateTextView.text = "Date goes here"
        binding.contentTextView.text = "Content goes here"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}