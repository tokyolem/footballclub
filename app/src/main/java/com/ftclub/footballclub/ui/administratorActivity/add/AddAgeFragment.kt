package com.ftclub.footballclub.ui.administratorActivity.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAddAgeBinding
import com.ftclub.footballclub.ui.registration.AgeFragmentArgs

/**
 * A simple [Fragment] subclass.
 * Use the [AddAgeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAgeFragment : Fragment() {

    private lateinit var _binding: FragmentAddAgeBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddAgeBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
        setCurrentDateOnView()
        insertAccount()
    }

    private fun insertAccount() {
        binding.addFinalButton.setOnClickListener {
            putAccountToDatabase()
        }
    }

    private fun putAccountToDatabase() {
        val arguments: AgeFragmentArgs by navArgs()
        val addData = arguments.regData

        dbViewModel.insertAccount(
            Accounts(
                addData[0], addData[1], false, DateTime.getFormatDateTime(),
                addData[2], addData[3], getUserAge(),
                addData[4], addData[5], addData[6]
            )
        )

        Toast.makeText(context, "Регистрация пройдена успешно!", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_addAgeFragment_to_navigation_accounts)
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
        binding.toAddInfPageFromAddAgePage.setOnClickListener {
            findNavController().navigate(R.id.action_ageFragment_to_informationFragment)
        }
    }

}