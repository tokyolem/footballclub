package com.ftclub.footballclub.ui.administratorActivity.players.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.databinding.PlayersCardBinding
import com.ftclub.footballclub.ui.adapter.BaseViewHolder
import com.ftclub.footballclub.ui.adapter.Item
import com.ftclub.footballclub.ui.adapter.ItemFingerprint

class PlayersFingerprint(
    private val cardClickAction: (item: Accounts) -> Unit
) : ItemFingerprint<PlayersCardBinding, Accounts> {

    override fun isRelativeItem(item: Item) = item is Accounts

    override fun getLayoutId() = R.layout.players_card

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<PlayersCardBinding, Accounts> {
        val binding = PlayersCardBinding.inflate(layoutInflater, parent, false)
        return PlayersViewHolder(binding, cardClickAction)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<Accounts>() {

        override fun areItemsTheSame(oldItem: Accounts, newItem: Accounts) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Accounts, newItem: Accounts) =
            oldItem == newItem

    }
}

class PlayersViewHolder(
    binding: PlayersCardBinding,
    private val cardClickAction: (item: Accounts) -> Unit
) : BaseViewHolder<PlayersCardBinding, Accounts>(binding) {

    override fun onBind(item: Accounts) {
        setPlayersContent(item)
        onCardClick(item)
    }

    private fun setPlayersContent(item: Accounts) {
        if (item.firstName.isEmpty()) {
            binding.userName.text = "<Не указано>"
            binding.userPhoneNumber.text = "<Не указано>"
            binding.playerPosition.text = "<Не указано>"
        } else {
            binding.userName.text = "${item.firstName} ${item.lastName}"
            binding.userPhoneNumber.text = item.phoneNumber
            binding.playerPosition.text = item.playerPosition
        }

        if (item.playerInformation.isEmpty()) {
            binding.playerInformation.text = "Игрок не указал информацию о себе"
        } else {
            binding.playerInformation.text = item.playerInformation
        }
    }

    private fun onCardClick(item: Accounts) {
        binding.playerCard.setOnClickListener {
            cardClickAction.invoke(item)
        }
    }
}
