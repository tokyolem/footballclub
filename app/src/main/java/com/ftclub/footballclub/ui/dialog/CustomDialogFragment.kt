package com.ftclub.footballclub.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.ftclub.footballclub.databinding.FragmentDialogBinding

class CustomDialogFragment(
    @StringRes private val title: Int,
    @StringRes private val message: Int,
    val action: (Unit) -> Unit
) : DialogFragment() {

    private lateinit var _binding: FragmentDialogBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.title.setText(title)
        binding.message.setText(message)

        binding.positive.setOnClickListener {
            action.invoke(Unit)
            dismiss()
        }
        binding.negative.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(950, LayoutParams.WRAP_CONTENT)
    }

}