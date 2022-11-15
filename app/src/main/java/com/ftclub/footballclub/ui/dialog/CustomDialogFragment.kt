package com.ftclub.footballclub.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomDialogFragment(
    @StringRes private val title: Int,
    @StringRes private val message: Int,
    private val actionPositive: (Unit) -> Unit,
    private val actionNegative: ((Unit) -> Unit)?
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ок") { _, _ -> actionPositive.invoke(Unit) }
            .setNegativeButton("Отменить") { _, _ -> actionNegative?.invoke(Unit)}
            .create()
    }
}