package com.jkearnsl.tempmail.ui.messages

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jkearnsl.tempmail.R
import com.jkearnsl.tempmail.TempMail
import com.jkearnsl.tempmail.databinding.FragmentMessageDetailBinding
import kotlinx.coroutines.launch

class MessageDetailFragment : Fragment() {

    private var _binding: FragmentMessageDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val app = requireActivity().application as TempMail
        val core = app.core

        val messageId = arguments?.getInt("messageId")
        binding.contentWebView.settings.javaScriptEnabled = true
        binding.contentWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                println("Error loading content: ${error?.description}")
            }
        }
        lifecycleScope.launch {
            val message = core.getMessage(messageId!!)
            binding.subjectTextView.text = message.subject
            binding.emailTextView.text = message.email
            binding.dateTextView.text = formatDate(message.date)

            val avatarTextView = binding.avatar
            val drawable = context?.let { ContextCompat.getDrawable(it, R.drawable.circle_background) } as GradientDrawable
            drawable.setColor(generateColorFromEmail(message.email))
            avatarTextView.background = drawable
            avatarTextView.text = message.email.first().toString().uppercase()

            binding.contentWebView.loadDataWithBaseURL(null, message.content, "text/html", "UTF-8", null)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}