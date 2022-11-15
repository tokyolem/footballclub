package com.ftclub.footballclub.ui.administratorActivity.players

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel

class PlayersFragmentViewModelFactory(
    private val application: Application,
    private val dbViewModel: AccountsViewModel
): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayersFragmentViewModel(application, dbViewModel) as T
    }
}