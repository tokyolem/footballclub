package com.ftclub.footballclub.ui.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts

class AccountsAdapter(private val accountsList: List<Accounts> ) :
    RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val accountsCardView = inflater.inflate(R.layout.accounts_card, parent, false)

        return AccountsViewHolder(accountsCardView)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val accounts: Accounts = accountsList[position]
        setUpAccountsView(accounts, holder)
    }

    override fun getItemCount(): Int = accountsList.size

    private fun setUpAccountsView(accounts: Accounts, viewHolder: AccountsViewHolder) {
        viewHolder.accountsCardView.findViewById<TextView>(R.id.account_card_title).text =
            accounts.accountEmail
        viewHolder.accountsCardView.findViewById<TextView>(R.id.date_time).text =
            accounts.dateTime
    }

    class AccountsViewHolder(val accountsCardView: View) : ViewHolder(accountsCardView)
}
