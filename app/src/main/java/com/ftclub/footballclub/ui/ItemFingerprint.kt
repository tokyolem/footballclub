package com.ftclub.footballclub.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts

interface ItemFingerprint<V : ViewBinding, I : Accounts> {

    fun isRelativeItem(item: Accounts): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<V, I>
}