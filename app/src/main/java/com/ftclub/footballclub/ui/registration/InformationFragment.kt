package com.ftclub.footballclub.ui.registration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.ui.ViewsAnimation
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 * Use the [InformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformationFragment : Fragment() {

    private lateinit var viewObjects: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_information, container, false)
        viewObjects = view

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
    }

    private fun navigation() {
        val toAgeFragmentButton =
            viewObjects.findViewById<FloatingActionButton>(R.id.to_age_fragment)
        val toRegistrationFragment =
            viewObjects.findViewById<FrameLayout>(R.id.to_reg_page_from_inf_page)

        toAgeFragmentButton.setOnClickListener {
            if (isInformationLinesEmpty()) {
                informationLinesEmpty()
            } else {
                val args: InformationFragmentArgs by navArgs()
                val toAgeFragment =
                    args.regData + getOwnAboutInformation() + getPlayerPosition() +
                            getPlayerFirstName() + getPlayerLastName() + getPlayerPhoneNumber()

                val action = InformationFragmentDirections.actionInformationFragmentToAgeFragment(
                    toAgeFragment
                )
                findNavController().navigate(action)
            }
        }

        toRegistrationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_informationFragment_to_registrationFragment)
        }
    }

    private fun getOwnAboutInformation(): String =
        viewObjects.findViewById<EditText>(R.id.own_about).text.toString()

    private fun getPlayerPosition(): String =
        viewObjects.findViewById<Spinner>(R.id.spinner).selectedItem.toString()

    private fun getPlayerFirstName(): String =
        viewObjects.findViewById<EditText>(R.id.first_name).text.toString()

    private fun getPlayerLastName(): String =
        viewObjects.findViewById<EditText>(R.id.last_name).text.toString()

    private fun getPlayerPhoneNumber(): String =
        viewObjects.findViewById<EditText>(R.id.phone_number).text.toString()

    private fun isInformationLinesEmpty(): Boolean {
        val firstName = viewObjects.findViewById<EditText>(R.id.first_name).text.toString()
        val lastName = viewObjects.findViewById<EditText>(R.id.last_name).text.toString()
        val phoneNumber = viewObjects.findViewById<EditText>(R.id.phone_number).text.toString()

        return firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()
    }

    private fun informationLinesEmpty() {
        val firstNameLine = viewObjects.findViewById<EditText>(R.id.first_name)
        val lastNameLine = viewObjects.findViewById<EditText>(R.id.last_name)
        val phoneNumberLine = viewObjects.findViewById<EditText>(R.id.phone_number)

        if (firstNameLine.text.toString().isEmpty()) {
            ViewsAnimation.propertyAnimationShow(firstNameLine, requireContext())
            ViewsAnimation.propertyAnimationHide(firstNameLine, requireContext())
        }
        if (lastNameLine.text.toString().isEmpty()) {
            ViewsAnimation.propertyAnimationShow(lastNameLine, requireContext())
            ViewsAnimation.propertyAnimationHide(lastNameLine, requireContext())
        }
        if (phoneNumberLine.text.toString().isEmpty()) {
            ViewsAnimation.propertyAnimationShow(phoneNumberLine, requireContext())
            ViewsAnimation.propertyAnimationHide(phoneNumberLine, requireContext())
        }
    }

}