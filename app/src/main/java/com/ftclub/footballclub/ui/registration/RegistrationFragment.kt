package com.ftclub.footballclub.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.BaseDatabase
import com.ftclub.footballclub.basic.room.accountsObject.Accounts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
        signUp()
        CoroutineScope(Dispatchers.Main).launch {
            val list = BaseDatabase.getAccountEmail("2")
            for (i in 0 until list){

            }
        }
    }

    private fun navigation() {
        val toSignInButton = requireActivity().findViewById<FrameLayout>(R.id.to_home_page_from_registration_page)
        toSignInButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }

    private fun signUp() {
        val signUpButton = requireActivity().findViewById<FrameLayout>(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            putUserFromDatabase()
        }
    }

    private fun isUserExisting(accountEmail: String): Boolean {
        val accountEmails = getAccountEmailsFromDatabase(accountEmail)

        for (email in accountEmails) {
            if (accountEmail == email.accountEmail) return true
        }

        return false
    }

    private fun getAccountEmailsFromDatabase(accountEmail: String): MutableList<Accounts> {
        val accountEmails = mutableListOf<Accounts>()
        return accountEmails
    }

    private fun putUserFromDatabase() {
        val userNewEmailLine = requireActivity().findViewById<EditText>(R.id.user_new_email)
        val userNewPasswordLine = requireActivity().findViewById<EditText>(R.id.user_new_password)
        val repeatPasswordLine = requireActivity().findViewById<EditText>(R.id.user_repeat_password)

        val currentNewEmail = userNewEmailLine.text.toString()
        val currentNewPassword = userNewPasswordLine.text.toString()
        val repeatPassword = repeatPasswordLine.text.toString()

        if (currentNewPassword != repeatPassword) {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        } else {
            if (isUserExisting(currentNewEmail)) {
                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
            } else {
                BaseDatabase.insertAccount(Accounts(currentNewEmail, 2, currentNewPassword))
            }
        }
    }
}