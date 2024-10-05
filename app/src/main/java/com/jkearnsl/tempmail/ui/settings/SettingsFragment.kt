package com.jkearnsl.tempmail.ui.settings

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jkearnsl.tempmail.TempMail
import com.jkearnsl.tempmail.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var rotationAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val app = requireActivity().application as TempMail
        val core = app.core

        startIconRotation()
        lifecycleScope.launch {
            binding.textInputEditText.setText(core.currentEmail())
            stopIconRotation()
        }


        binding.textInputLayout.setEndIconOnClickListener {
            startIconRotation()
            lifecycleScope.launch {
                binding.textInputEditText.setText(core.generateRandomEmail())
                stopIconRotation()
            }
        }
        return root
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun startIconRotation() {
        val endIconView = binding.textInputLayout.findViewById<View>(com.google.android.material.R.id.text_input_end_icon)
        endIconView?.let {
            rotationAnimator = ObjectAnimator.ofFloat(it, "rotation", 0f, -360f).apply {
                duration = 1000 // 1 second
                repeatCount = ObjectAnimator.INFINITE
                start()
            }
        }
    }

    private fun stopIconRotation() {
        rotationAnimator?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}