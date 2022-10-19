package com.ftclub.footballclub.ui.players

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.databinding.PlayersCardBinding
import com.ftclub.footballclub.ui.BaseViewHolder
import com.ftclub.footballclub.ui.ItemFingerprint

class PlayersFingerprint(
    private val context: Context
) : ItemFingerprint<PlayersCardBinding, Accounts> {

    override fun isRelativeItem(item: Accounts) = true

    override fun getLayoutId() = R.layout.players_card

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<PlayersCardBinding, Accounts> {
        val binding = PlayersCardBinding.inflate(layoutInflater, parent, false)
        return PlayersViewHolder(binding, context)
    }
}

class PlayersViewHolder(
    binding: PlayersCardBinding,
    private val context: Context
) : BaseViewHolder<PlayersCardBinding, Accounts>(binding) {

    override fun onBind(item: Accounts) {
        voidNotice(item)
    }

    private fun voidNotice(item: Accounts) {
        if (item.firstName.isEmpty()) {
            binding.notice.setBackgroundColor(
                context.resources.getColor(
                    R.color.logout_color,
                    context.resources.newTheme()
                )
            )

            binding.checkAccount.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_exclamation,
                    context.resources.newTheme()
                )
            )

            binding.playerCardTitle.text = "<Не указано>"
            binding.phone.text = "<Не указано>"
        } else {
            binding.notice.setBackgroundColor(
                context.resources.getColor(
                    R.color.AppBarColor,
                    context.resources.newTheme()
                )
            )

            binding.checkAccount.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_check,
                    context.resources.newTheme()
                )
            )

            binding.playerCardTitle.text = "${item.firstName} ${item.lastName}"
            binding.phone.text = "Номер телефона: ${item.phoneNumber}"
        }

    }
}
