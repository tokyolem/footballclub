package com.ftclub.footballclub.ui.administratorActivity.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAddInformationBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.administratorActivity.AdministratorActivity
import com.ftclub.footballclub.ui.signInActivity.registration.InformationFragmentArgs
import com.ftclub.footballclub.ui.signInActivity.registration.InformationFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

/**
 * A simple [Fragment] subclass.
 * Use the [AddInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddInformationFragment : Fragment() {

    private lateinit var _binding: FragmentAddInformationBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddInformationBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        setBottomSheetExpandState(BottomSheetBehavior.STATE_COLLAPSED)
        setBottomNavViewVisibility(View.GONE)

        onBackClick()
        onSetAgeClick()
        onConfirmAddingClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setAdapterToPositionsLayout()
    }

    private fun onSetAgeClick() {
        binding.addAge.setOnClickListener {
            setBottomSheetExpandState(BottomSheetBehavior.STATE_EXPANDED)
            ViewsAnimation.viewScaleDown(binding.addNewAccount, requireContext())
        }

        binding.applySetAge.setOnClickListener {
            setBottomSheetExpandState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    private fun setBottomSheetExpandState(mState: Int) {
        BottomSheetBehavior.from(binding.ageBottomSheet).apply {
            peekHeight = 0
            this.state = mState
        }

        BottomSheetBehavior.from(binding.ageBottomSheet)
            .addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) ViewsAnimation.viewScaleUp(
                        binding.addNewAccount, requireContext()
                    )
                }
            })
    }

    private fun setAdapterToPositionsLayout() {
        val arrayAdapter = ArrayAdapter(
            requireContext(), R.layout.dropdown_menu,
            resources.getStringArray(R.array.positions)
        )
        binding.playerPositionSelector.setAdapter(arrayAdapter)
    }

    private fun onBackClick() {
        binding.back.setOnClickListener {
            (activity as AdministratorActivity).onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onConfirmAddingClick() {
        binding.addNewAccount.setOnClickListener {
            addingProcess()
        }
    }

    private fun addingProcess() {
        val args: AddInformationFragmentArgs by navArgs()

        if (informationLinesEmpty()) return
        else {
            dbViewModel.insertAccount(
                Accounts(
                    args.regData[0], args.regData[1], false,
                    DateTime.getFormatDateTime(), getPlayerInformation(), getPlayerPosition(),
                    getPlayerAge(), getPlayerFirstName(), getPlayerLastName(), getPlayerPhoneNumber()
                )
            )

            findNavController().navigate(
                AddInformationFragmentDirections.actionAddInformationFragmentToNavigationAccounts()
            )
        }
    }

    private fun getPlayerInformation() =
        binding.playerInformationLine.text.toString()

    private fun getPlayerPosition() =
        binding.playerPositionSelector.text.toString()

    private fun getPlayerFirstName() =
        binding.firstNameLine.text.toString()

    private fun getPlayerLastName() =
        binding.lastNameLine.text.toString()

    private fun getPlayerPhoneNumber() =
        "${binding.phoneNumberLayout.prefixText} ${binding.phoneNumberLine.text.toString()}"

    private fun getPlayerAge(): String {
        val newYear = binding.datePicker.year
        val setMonth = binding.datePicker.month
        val setDay = binding.datePicker.dayOfMonth

        var newMonth = ""
        var newDay = ""

        when (setMonth) {
            in 0..9 -> newMonth = "0${setMonth + 1}"
            in 10..12 -> newMonth = setMonth.toString()
        }

        when (setDay) {
            in 0..9 -> newDay = "0${setDay}"
            in 10..31 -> newDay = setDay.toString()
        }

        return "$newDay/$newMonth/$newYear"
    }

    private fun informationLinesEmpty(): Boolean {
        return when (isInformationLinesEmpty()) {
            true -> {
                Toast.makeText(
                    requireContext(),
                    "Заполните все поля, отмеченные знаком*",
                    Toast.LENGTH_LONG
                )
                    .show()
                true
            }
            false -> false
        }
    }

    private fun isInformationLinesEmpty() =
        binding.firstNameLine.text.toString().isEmpty()
                || binding.lastNameLine.text.toString().isEmpty()
                || binding.phoneNumberLine.text.toString().isEmpty()


    private fun setBottomNavViewVisibility(visibility: Int) {
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            .visibility = visibility
    }

    override fun onStop() {
        super.onStop()
        setBottomNavViewVisibility(View.VISIBLE)
    }
}