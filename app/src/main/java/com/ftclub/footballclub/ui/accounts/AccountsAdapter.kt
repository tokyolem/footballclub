package com.ftclub.footballclub.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.ui.BaseViewHolder
import com.ftclub.footballclub.ui.ItemFingerprint
import com.google.android.material.card.MaterialCardView

class AccountsAdapter(
    private val accounts: List<ItemFingerprint<*, *>>,
) : RecyclerView.Adapter<BaseViewHolder<ViewBinding, Accounts>>() {

    private var items = emptyList<Accounts>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, Accounts> {
        val inflater = LayoutInflater.from(parent.context)
        return accounts.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater,parent)
            ?.let { it as BaseViewHolder<ViewBinding, Accounts> }
            ?: throw java.lang.IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Accounts>, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return accounts.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw java.lang.IllegalArgumentException("View type not found: $item")
    }

    fun setData(accounts: List<Accounts>) {
        this.items = accounts
        items.reversed()
        notifyDataSetChanged()
    }
}


