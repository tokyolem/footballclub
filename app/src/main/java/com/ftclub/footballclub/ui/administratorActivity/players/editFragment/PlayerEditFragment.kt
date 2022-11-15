package com.ftclub.footballclub.ui.administratorActivity.players.editFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayerEditBinding
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class PlayerEditFragment : Fragment() {

    private var _binding: FragmentPlayerEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbViewModel: AccountsViewModel

    private var isDatePickerUsed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerEditBinding.inflate(inflater, container, false)

        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
        initDatePicker()
        navigation()
        setAccountContent()
        onAgeButtonsClick()

        return binding.root
    }

    private fun initDatePicker() {
        binding.datePicker.maxDate = System.currentTimeMillis() + 1_000_000

        val date = Date()
        val calendar = GregorianCalendar()
        calendar.time = date

        binding.datePicker.init(
            calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        ) { _, _, _, _ ->
            isDatePickerUsed = true
        }
    }

    private fun setBottomSheetState(mState: Int) {
        BottomSheetBehavior.from(binding.ageBottomSheet).apply {
            peekHeight = 0
            state = mState
        }
    }

    private fun navigation() {
        binding.close.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onAgeButtonsClick() {
        binding.addAge.setOnClickListener {
            setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
        }

        binding.applySetAge.setOnClickListener {
            setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    private fun saveChanges(account: Accounts) {
        binding.saveEdits.setOnClickListener {
            CustomDialogFragment(
                R.string.edit_title,
                R.string.edit_message,
                {
                    dbViewModel.updateAccount(
                        Accounts(
                            account.accountEmail, account.hashPassword, account.accountRole,
                            account.dateTime, getPlayerInformation(), getPlayerPosition(),
                            getPlayerAge(account), getPlayerFirstName(), getPlayerLastName(),
                            getPlayerPhoneNumber(), account.id
                        )
                    )
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }, null
            )
                .show(requireActivity().supportFragmentManager, "custom_dialog")
        }
    }

    private fun setAccountContent() {
        val args: PlayerEditFragmentArgs by navArgs()
        dbViewModel.getAccountById(args.accountId.toLong())

        dbViewModel.accountById.observe(viewLifecycleOwner) { account ->
            verifyForInformationEmpty(account)
            saveChanges(account)
        }
    }

    private fun verifyForInformationEmpty(account: Accounts) {
        if (account.firstName.isEmpty()) {
            binding.firstNameLine.setText("<Не указано>")
            binding.lastNameLine.setText("<Не указано>")
            binding.phoneNumberLine.setText("<Не указано>")
            binding.playerPositionSelector.setText("<Не указано>")
        } else {
            binding.firstNameLine.setText(account.firstName)
            binding.lastNameLine.setText(account.lastName)
            binding.phoneNumberLine.setText(account.phoneNumber)
            binding.playerPositionSelector.setText(account.playerPosition)
        }

        if (account.playerInformation.isEmpty()) {
            binding.playerInformationLine.setText("")
        } else {
            binding.playerInformationLine.setText(account.playerInformation)
        }
    }

    private fun getPlayerFirstName() = binding.firstNameLine.text.toString()

    private fun getPlayerLastName() = binding.lastNameLine.text.toString()

    private fun getPlayerPhoneNumber() = binding.phoneNumberLine.text.toString()

    private fun getPlayerPosition() = binding.playerPositionSelector.text.toString()

    private fun getPlayerInformation() = binding.playerInformationLine.text.toString()

    private fun getPlayerAge(account: Accounts): String {
        return if (isDatePickerUsed) DateTime.formatPlayerAge(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth
        )
        else account.playerAge
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}