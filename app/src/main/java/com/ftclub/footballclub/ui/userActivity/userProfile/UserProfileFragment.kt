package com.ftclub.footballclub.ui.userActivity.userProfile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentUserProfileBinding
import com.google.android.material.navigation.NavigationView

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        closeNavDrawer()
        onBackClick()
        setAccountContent()

        return binding.root
    }

    private fun setAccountContent() {
        val args: UserProfileFragmentArgs by navArgs()
        dbViewModel.getAccountById(args.playerId)

        dbViewModel.accountById.observe(viewLifecycleOwner) { currentAccount ->
            currentAccount?.let {
                binding.email.text = currentAccount.accountEmail
                verifyAccountContentForEmptiness(currentAccount)
            }
        }
    }

    private fun verifyAccountContentForEmptiness(account: Accounts) {
        if (account.firstName.isEmpty()) {
            binding.FI.text = "<Не указано>"
            binding.position.text = "<Не указано>"
            binding.birthday.text = "<Не указано>"
            binding.phoneNumber.text = "<Не указано>"
        } else {
            binding.FI.text = "${account.firstName} ${account.lastName}"
            binding.position.text = account.playerPosition
            binding.birthday.text = account.playerAge
            binding.phoneNumber.text = account.phoneNumber
        }

        if (account.playerInformation.isEmpty()) binding.playerInformation.text =
            "Вы не указали информацию о себе"
        else binding.playerInformation.text = account.playerInformation
    }

    private fun onBackClick() {
        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun closeNavDrawer() {
        val navDrawerMenu = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout_user)
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view_user)

        navDrawerMenu.close()
        setAbilityToOpenNavDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, navDrawerMenu, navView)
    }

    private fun setAbilityToOpenNavDrawer(
        lockMode: Int,
        navDrawer: DrawerLayout,
        navView: NavigationView
    ) {
        navDrawer.setDrawerLockMode(lockMode, navView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}