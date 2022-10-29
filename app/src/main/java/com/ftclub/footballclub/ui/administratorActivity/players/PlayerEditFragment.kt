package com.ftclub.footballclub.ui.administratorActivity.players

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.ui.administratorActivity.AdministratorActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayerEditBinding
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlayerEditFragment : Fragment() {

    private lateinit var _binding: FragmentPlayerEditBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerEditBinding.inflate(inflater, container, false)

        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        navigation()
        setAccountContent()

        return binding.root
    }

    private fun navigation() {
        binding.toPlayerPageFromEditPage.setOnClickListener {
            (activity as AdministratorActivity).onBackPressed()
        }
    }

    private fun saveChanges(account: Accounts) {
        binding.saveChanges.setOnClickListener {
            val dialog = CustomDialogFragment(
                R.string.edit_title,
                R.string.edit_message,
            ) {
                dbViewModel.updateAccount(
                    Accounts(
                        getPlayerEmail(), account.hashPassword, account.accountRole,
                        account.dateTime, account.playerInformation, account.playerPosition,
                        account.playerAge, getPlayerFirstName(), getPlayerLastName(),
                        getPlayerPhoneNumber(), account.id
                    )
                )
                (activity as AdministratorActivity).onBackPressed()
            }
            dialog.show(requireActivity().supportFragmentManager, "custom_dialog")
        }
    }

    private fun setAccountContent() {
        val args: com.ftclub.footballclub.ui.players.PlayerEditFragmentArgs by navArgs()
        dbViewModel.getAccountById(args.accountId.toLong())

        dbViewModel.accountById.observe(viewLifecycleOwner) { account ->
            binding.playerEmail.setText(account.accountEmail)
            verifyForInformationEmpty(account)
            saveChanges(account)
        }
    }

    private fun verifyForInformationEmpty(account: Accounts) {
        if (account.firstName.isEmpty()) {
            binding.playerFirstName.setText("<Не указано>")
            binding.playerLastName.setText("<Не указано>")
            binding.playerPhoneNumber.setText("<Не указано>")
        } else {
            binding.playerFirstName.setText(account.firstName)
            binding.playerLastName.setText(account.lastName)
            binding.playerPhoneNumber.setText(account.phoneNumber)
        }
    }

    private fun getPlayerFirstName() = binding.playerFirstName.text.toString()

    private fun getPlayerLastName() = binding.playerLastName.text.toString()

    private fun getPlayerEmail() = binding.playerEmail.text.toString()

    private fun getPlayerPhoneNumber() = binding.playerPhoneNumber.text.toString()
}