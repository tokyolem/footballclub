package com.ftclub.footballclub.ui.userActivity.userPlayers

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel

class UserPlayersFragmentViewModel(
    application: Application,
    private val dbViewModel: AccountsViewModel
) : AndroidViewModel(application) {

    fun searchItem(searchableString: String): List<Accounts> {
        val accountsList = dbViewModel.accountsLiveData.value as List<Accounts>
        val searchableItem = mutableListOf<Accounts>()

        for (account in accountsList) {
            if (account.accountEmail.lowercase().contains(searchableString.lowercase())
                || account.firstName.lowercase().contains(searchableString.lowercase())
                || account.lastName.lowercase().contains(searchableString.lowercase())
                || account.phoneNumber.lowercase().contains(searchableString.lowercase())
                || account.playerPosition.lowercase().contains(searchableString.lowercase())
            )
                searchableItem.add(account)
        }

        return if (searchableItem.isEmpty()) {
            Toast.makeText(getApplication(), "Ничего не найдено!", Toast.LENGTH_LONG).show()
            emptyList()
        } else {
            searchableItem
        }
    }

}