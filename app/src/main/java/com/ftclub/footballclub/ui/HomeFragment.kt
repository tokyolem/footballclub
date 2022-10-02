package com.ftclub.footballclub.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        buttonActions()
    }

    private fun buttonActions() {
        val toAuthorizationFragment =
            requireActivity().findViewById<FrameLayout>(R.id.to_authorization_page)
        val toRegistrationFragment =
            requireActivity().findViewById<FrameLayout>(R.id.to_registration_page)

        toAuthorizationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_authorizationFragment)
        }

        toRegistrationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registrationFragment)
        }
    }
}