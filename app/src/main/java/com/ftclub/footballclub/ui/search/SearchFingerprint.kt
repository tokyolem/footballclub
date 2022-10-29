package com.ftclub.footballclub.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.AccountsCardBinding
import com.ftclub.footballclub.databinding.PlayersCardBinding
import com.ftclub.footballclub.ui.BaseViewHolder
import com.ftclub.footballclub.ui.ItemFingerprint
import com.ftclub.footballclub.ui.administratorActivity.accounts.AccountsViewHolder
import com.ftclub.footballclub.ui.administratorActivity.players.PlayersViewHolder

class SearchFingerprint(
    private val searchType: String,
    private val rvView: ViewGroup,
    private val context: Context,
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val dbViewModel: AccountsViewModel,
    private val isSearchFragment: Boolean
    ) : ItemFingerprint<ViewBinding, Accounts> {

    private val SEARCH_ACCOUNTS = "accounts"
    private val SEARCH_PLAYERS = "players"

    override fun isRelativeItem(item: Accounts) = true

    override fun getLayoutId(): Int {
        return when (searchType) {
            SEARCH_ACCOUNTS -> R.id.account_card
            SEARCH_PLAYERS -> R.id.players_card
            else -> throw IllegalArgumentException("Unable to determine layout type: $searchType")
        }
    }

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ViewBinding, Accounts> {
        return when (searchType) {
            SEARCH_ACCOUNTS -> {
                val binding = AccountsCardBinding.inflate(layoutInflater, parent, false)
                AccountsViewHolder(binding, rvView, context, activity, dbViewModel)
            }
            SEARCH_PLAYERS -> {
                val binding = PlayersCardBinding.inflate(layoutInflater, parent, false)
                PlayersViewHolder(binding, context, fragment, isSearchFragment)
            }
            else -> throw IllegalArgumentException("Unable to get viewHolder type: $searchType")
        }
    }

}