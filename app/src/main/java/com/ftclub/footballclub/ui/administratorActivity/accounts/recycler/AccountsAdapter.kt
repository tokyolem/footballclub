package com.ftclub.footballclub.ui.administratorActivity.accounts.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.ftclub.footballclub.ui.adapter.diffUtil.FingerprintDiffUtil
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.ui.adapter.BaseViewHolder
import com.ftclub.footballclub.ui.adapter.Item
import com.ftclub.footballclub.ui.adapter.ItemFingerprint

class AccountsAdapter(
    private val fingerprints: List<ItemFingerprint<*, *>>,
) : ListAdapter<Item, BaseViewHolder<ViewBinding, Item>>(
    FingerprintDiffUtil(fingerprints)
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding, Item> {
        val inflater = LayoutInflater.from(parent.context)
        return fingerprints.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, Item> }
            ?: throw java.lang.IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Item>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw java.lang.IllegalArgumentException("View type not found: $item")
    }


    override fun getItemId(position: Int) =
        (getItem(position) as Accounts).id!!
}


