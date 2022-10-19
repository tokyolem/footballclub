package com.ftclub.footballclub.ui.registration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAgeBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * A simple [Fragment] subclass.
 * Use the [AgeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgeFragment : Fragment() {

    private lateinit var _binding: FragmentAgeBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgeBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
        setCurrentDateOnView()
        signUp()
    }

    private fun signUp() {
        binding.signUpFinalButton.setOnClickListener {
            putAccountToDatabase()
        }
    }

    private fun putAccountToDatabase() {
        val arguments: AgeFragmentArgs by navArgs()
        val registrationData = arguments.regData

        dbViewModel.insertAccount(
            Accounts(
                registrationData[0], registrationData[1], false, DateTime.getFormatDateTime(),
                registrationData[2], registrationData[3], getUserAge(),
                registrationData[4], registrationData[5], registrationData[6]
            )
        )

        Toast.makeText(context, "Регистрация пройдена успешно!", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_ageFragment_to_authorizationFragment)
    }

    private fun setCurrentDateOnView() = run {
        binding.datePicker.maxDate =
            System.currentTimeMillis()
    }

    private fun getUserAge(): String {
        val datePicker = binding.datePicker

        val year = datePicker.year.toString()
        var month = datePicker.month.toString()
        var day = datePicker.dayOfMonth.toString()

        if (month in "1".."9"){
            month = month.replace(month, "0$month")
        }

        if (day in "1".."9"){
            day = day.replace(month, "0$day")
        }

        return "$day/$month/$year"
    }

    private fun navigation() {
        binding.toInfPageFromAgePage.setOnClickListener {
            findNavController().navigate(R.id.action_ageFragment_to_informationFragment)
        }
    }
}