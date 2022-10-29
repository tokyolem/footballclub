package com.ftclub.footballclub.ui.administratorActivity.accounts

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.AccountsCardBinding
import com.ftclub.footballclub.ui.BaseViewHolder
import com.ftclub.footballclub.ui.ItemFingerprint
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment

class AccountFingerprint(
    private val rvView: ViewGroup,
    private val context: Context,
    private val activity: FragmentActivity,
    private val dbViewModel: AccountsViewModel
) : ItemFingerprint<AccountsCardBinding, Accounts> {

    override fun isRelativeItem(item: Accounts) = true

    override fun getLayoutId() = R.layout.accounts_card

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<AccountsCardBinding, Accounts> {
        val binding = AccountsCardBinding.inflate(layoutInflater, parent, false)
        return AccountsViewHolder(binding, rvView, context, activity, dbViewModel)
    }
}

class AccountsViewHolder(
    binding: AccountsCardBinding,
    private val rvView: ViewGroup,
    private val context: Context,
    private val activity: FragmentActivity,
    private val dbViewModel: AccountsViewModel
) : BaseViewHolder<AccountsCardBinding, Accounts>(binding) {

    override fun onBind(item: Accounts) {
        binding.accountCardTitle.text = "Email: ${item.accountEmail}"
        binding.dateTime.text = "Дата регистрации: ${item.dateTime}"
        binding.accountEditEmail.setText(item.accountEmail)
        binding.accountEditPassword.setText(item.hashPassword)

        if (item.firstName.isEmpty()) binding.userNameCard.text = "<Не указано>"
        else binding.userNameCard.text = "Имя: ${item.firstName}"

        if (item.phoneNumber.isEmpty()) binding.userPhoneCard.text = "<Не указано>"
        else binding.userPhoneCard.text = "Номер телефона: ${item.phoneNumber}"

        accountCardExpandedAnimation()
        onRemoveAccountButtonClick(item)
    }

    private fun accountCardExpandedAnimation() {
        binding.accountCard.setOnClickListener {
            TransitionManager.beginDelayedTransition(rvView as ViewGroup?, AutoTransition())
            if (binding.accountCardExpandableLayout.visibility == View.VISIBLE) {
                binding.accountCardExpandableLayout.visibility = View.GONE
                Handler(Looper.getMainLooper()).postDelayed({
                    ViewsAnimation.arrowCardUpToDownAnimation(binding.arrow, context)
                }, 100)
            } else {
                binding.accountCardExpandableLayout.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    ViewsAnimation.arrowCardDownToUpAnimation(binding.arrow, context)
                }, 100)

            }
        }
    }

    private fun onRemoveAccountButtonClick(item: Accounts) {
        if (item.accountEmail == "admin") binding.accountDelete.visibility = View.GONE

        binding.accountDelete.setOnClickListener {
            val dialog = CustomDialogFragment(
                R.string.delete_title,
                R.string.delete_message
            ) { dbViewModel.deleteAccount(item.id!!) }
            dialog.show(activity.supportFragmentManager, "custom_dialog")
        }
    }
}