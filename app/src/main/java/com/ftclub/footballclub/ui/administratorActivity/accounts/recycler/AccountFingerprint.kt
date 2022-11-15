package com.ftclub.footballclub.ui.administratorActivity.accounts

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.AccountsCardBinding
import com.ftclub.footballclub.databinding.FragmentAccountsBinding
import com.ftclub.footballclub.ui.BaseViewHolder
import com.ftclub.footballclub.ui.ItemFingerprint
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

class AccountFingerprint(
    private val fragmentBinding: FragmentAccountsBinding,
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
        return AccountsViewHolder(binding, fragmentBinding, context, activity, dbViewModel)
    }
}

class AccountsViewHolder(
    binding: AccountsCardBinding,
    private val fragmentBinding: FragmentAccountsBinding,
    private val context: Context,
    private val activity: FragmentActivity,
    private val dbViewModel: AccountsViewModel
) : BaseViewHolder<AccountsCardBinding, Accounts>(binding) {

    override fun onBind(item: Accounts) {
        setAccountsContent(item)
        onMoreButtonClick(item)
    }

    private fun setAccountsContent(item: Accounts) {
        binding.accountEmail.text = item.accountEmail
        binding.regDate.text = "Дата записи: ${item.dateTime}"

        if (item.firstName.isEmpty()) binding.userName.text = "<Не указано>"
        else binding.userName.text = item.firstName

        if (item.playerPosition.isEmpty()) binding.position.text = "<Не указано>"
        else binding.position.text = item.playerPosition
    }

    private fun onMoreButtonClick(item: Accounts) {
        binding.more.setOnClickListener {
            openPopupActionsMenu(item)
        }
    }

    private fun openPopupActionsMenu(item: Accounts) {
        val actionsMenu = PopupMenu(context, binding.more)
        actionsMenu.menuInflater.inflate(R.menu.account_action_menu, actionsMenu.menu)

        actionsMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.edit_account) {
                openEditBottomSheet(item)
                dimmerVisibility(View.VISIBLE)
                bottomNavViewVisibility(View.GONE)
            } else {
                CustomDialogFragment(R.string.delete_title, R.string.delete_message) {
                    dbViewModel.deleteAccount(item.id!!)
                }.show(activity.supportFragmentManager, "custom_dialog")

            }
            true
        }

        try {
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(actionsMenu)
            menu.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            actionsMenu.show()
            bottomSheetCallback()
        }
    }

    private fun bottomSheetCallback() {
        BottomSheetBehavior.from(fragmentBinding.bottomSheetAccounts)
            .addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (BottomSheetBehavior.from(fragmentBinding.bottomSheetAccounts).state
                        == BottomSheetBehavior.STATE_COLLAPSED
                    ) {
                        dimmerVisibility(View.GONE)
                        bottomNavViewVisibility(View.VISIBLE)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
    }

    private fun openEditBottomSheet(item: Accounts) {
        setAccountContentToBottomSheet(item)
        BottomSheetBehavior.from(fragmentBinding.bottomSheetAccounts).apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun dimmerVisibility(visibility: Int) {
        when (visibility) {
            View.VISIBLE ->
                ViewsAnimation.viewShowAnimation(
                    fragmentBinding.translucentBlackBackground,
                    context
                )
            View.GONE -> ViewsAnimation.viewHideAnimation(
                fragmentBinding.translucentBlackBackground,
                context
            )
        }

        fragmentBinding.translucentBlackBackground.visibility = visibility

    }

    private fun bottomNavViewVisibility(visibility: Int) {
        activity.findViewById<BottomNavigationView>(R.id.nav_view).visibility = visibility
    }

    private fun setAccountContentToBottomSheet(item: Accounts) {
        fragmentBinding.accountEditEmail.setText(item.accountEmail)
        fragmentBinding.accountEditPassword.setText(item.hashPassword)
    }
}