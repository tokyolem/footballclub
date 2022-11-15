package com.ftclub.footballclub.ui.administratorActivity.accounts.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.databinding.AccountsCardBinding
import com.ftclub.footballclub.ui.adapter.BaseViewHolder
import com.ftclub.footballclub.ui.adapter.Item
import com.ftclub.footballclub.ui.adapter.ItemFingerprint

class AccountFingerprint(
    val action: (item: Accounts, view: View) -> Unit
) : ItemFingerprint<AccountsCardBinding, Accounts> {

    override fun isRelativeItem(item: Item) = item is Accounts

    override fun getLayoutId() = R.layout.accounts_card

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<AccountsCardBinding, Accounts> {
        val binding = AccountsCardBinding.inflate(layoutInflater, parent, false)
        return AccountsViewHolder(binding, action)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<Accounts>() {

        override fun areContentsTheSame(oldItem: Accounts, newItem: Accounts) =
            oldItem.id == newItem.id

        override fun areItemsTheSame(oldItem: Accounts, newItem: Accounts) =
            oldItem == newItem
    }
}

class AccountsViewHolder(
    binding: AccountsCardBinding,
    val action: (item: Accounts, view: View) -> Unit
) : BaseViewHolder<AccountsCardBinding, Accounts>(binding) {

    override fun onBind(item: Accounts) {
        setAccountsContent(item)
        onMoreButtonClick(item)
    }

    private fun setAccountsContent(item: Accounts) {
        binding.accountEmail.text = item.accountEmail
        binding.regDate.text = "Дата записи: ${item.dateTime}"

        if (item.firstName.isEmpty()){
            binding.userName.text = "<Не указано>"
            binding.position.text = "<Не указано>"
        } else {
            binding.userName.text = item.firstName
            binding.position.text = item.playerPosition
        }
    }

    private fun onMoreButtonClick(item: Accounts) {
        binding.more.setOnClickListener {
            action.invoke(item, binding.more)
        }
    }

}