package com.ftclub.footballclub.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel

class CustomDialogFragment(private val item: Accounts) : DialogFragment() {

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return builder.setTitle(R.string.delete_title).setMessage(R.string.delete_message)
            .setPositiveButton("Да") { _, _ ->
                dbViewModel.deleteAccount(item.id!!)
            }.setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }.create()
    }
}