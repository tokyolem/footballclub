package com.ftclub.footballclub.ui.signInActivity.registration

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
import com.ftclub.footballclub.databinding.FragmentInformationBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 * Use the [InformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformationFragment : Fragment() {

    private lateinit var _binding: FragmentInformationBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
    }

    private fun navigation() {
        binding.toAgeFragment.setOnClickListener {
            if (isInformationLinesEmpty()) {
                informationLinesEmpty()
            } else {
                val args: InformationFragmentArgs by navArgs()
                val toAgeFragment =
                    args.regData + getOwnAboutInformation() + getPlayerPosition() +
                            getPlayerFirstName() + getPlayerLastName() + getPlayerPhoneNumber()

                val action =
                    InformationFragmentDirections.actionInformationFragmentToAgeFragment(
                        toAgeFragment
                    )
                findNavController().navigate(action)
            }
        }

        binding.toRegPageFromInfPage.setOnClickListener {
            findNavController().navigate(R.id.action_informationFragment_to_registrationFragment)
        }
    }

    private fun getOwnAboutInformation() =
        binding.ownAbout.text.toString()

    private fun getPlayerPosition() =
       binding.spinner.selectedItem.toString()

    private fun getPlayerFirstName() =
        binding.firstName.text.toString()

    private fun getPlayerLastName() =
        binding.lastName.text.toString()

    private fun getPlayerPhoneNumber() =
        binding.phoneNumber.text.toString()

    private fun isInformationLinesEmpty() =
        binding.firstName.text.toString().isEmpty()
                || binding.lastName.text.toString().isEmpty()
                || binding.phoneNumber.text.toString().isEmpty()

    private fun informationLinesEmpty() {
        val firstNameLine = binding.firstName
        val lastNameLine = binding.lastName
        val phoneNumberLine = binding.phoneNumber

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