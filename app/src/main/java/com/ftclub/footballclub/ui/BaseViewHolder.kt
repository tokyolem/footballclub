package com.ftclub.footballclub.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts

abstract class BaseViewHolder<out V : ViewBinding, in I : Accounts>(
    val binding: V
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(item: I)
}