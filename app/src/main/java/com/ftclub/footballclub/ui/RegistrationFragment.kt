package com.ftclub.footballclub.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R

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
    }

    private fun navigation() {
        val toSignInButton = requireActivity().findViewById<FrameLayout>(R.id.to_home_page_from_registration_page)
        toSignInButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }

    private fun getExistingUser(): Boolean {
        var userName: String? = null

        //TODO: в случае, если пользователь уже существует - вернуть true

        return false
    }

    private fun putUserFromDatabase() {
        val userNewEmailLine = requireActivity().findViewById<EditText>(R.id.user_new_email)
        val userNewPasswordLine = requireActivity().findViewById<EditText>(R.id.user_new_password)
        val repeatPasswordLine = requireActivity().findViewById<EditText>(R.id.user_repeat_password)

        val currentNewEmail = userNewEmailLine.text.toString()
        val currentNewPassword = userNewPasswordLine.text.toString()
        val repeatPassword = repeatPasswordLine.text.toString()

        if (currentNewPassword != repeatPassword) {
            //TODO: вывести ошибку о несовпадении
            //TODO: Как реализованно?
        } else {
            if (getExistingUser()) {
                //TODO: вывести ошибку о том, что пользователь уже существует
            } else {
                //TODO: всё в порядке, можно добавлять
            }
        }
    }
}