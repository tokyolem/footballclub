package com.ftclub.footballclub.ui.administratorActivity.players

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.databinding.PlayersCardBinding
import com.ftclub.footballclub.ui.BaseViewHolder
import com.ftclub.footballclub.ui.ItemFingerprint
import com.ftclub.footballclub.ui.players.PlayersFragmentDirections
import com.ftclub.footballclub.ui.search.SearchFragment
import com.ftclub.footballclub.ui.search.SearchFragmentDirections


class PlayersFingerprint(
    private val context: Context,
    private val fragment: Fragment,
    private val isSearchFragment: Boolean
) : ItemFingerprint<PlayersCardBinding, Accounts> {

    override fun isRelativeItem(item: Accounts) = true

    override fun getLayoutId() = R.layout.players_card

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<PlayersCardBinding, Accounts> {
        val binding = PlayersCardBinding.inflate(layoutInflater, parent, false)
        return PlayersViewHolder(binding, context, fragment, isSearchFragment)
    }
}

class PlayersViewHolder(
    binding: PlayersCardBinding,
    private val context: Context,
    private val fragment: Fragment,
    private val isSearchFragment: Boolean
) : BaseViewHolder<PlayersCardBinding, Accounts>(binding) {

    override fun onBind(item: Accounts) {
        voidNotice(item)
        onCardClick(item)
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

    private fun onCardClick(item: Accounts) {
        binding.playersCard.setOnClickListener {
            if (isSearchFragment) {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToPlayerPageFragment(item.id!!.toInt())
                fragment.findNavController().navigate(action)
            } else {
                val action =
                    PlayersFragmentDirections.actionNavigationPlayersToPlayerPageFragment(item.id!!.toInt())
                fragment.findNavController().navigate(action)
            }
        }
    }
}
