package com.ftclub.footballclub.ui.administratorActivity.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.databinding.FragmentAddInformationBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.registration.InformationFragmentArgs
import com.ftclub.footballclub.ui.registration.InformationFragmentDirections

/**
 * A simple [Fragment] subclass.
 * Use the [AddInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddInformationFragment : Fragment() {

    private lateinit var _binding: FragmentAddInformationBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
    }

    private fun navigation() {
        binding.toAddAgeFragment.setOnClickListener {
            if (isInformationLinesEmpty()) {
                informationLinesEmpty()
            } else {
                val args: com.ftclub.footballclub.ui.add.AddInformationFragmentArgs by navArgs()
                val toAgeFragment =
                    args.regData + getOwnAboutInformation() + getPlayerPosition() +
                            getPlayerFirstName() + getPlayerLastName() + getPlayerPhoneNumber()

                val action =
                    com.ftclub.footballclub.ui.add.AddInformationFragmentDirections.actionAddInformationFragmentToAddAgeFragment(
                        toAgeFragment
                    )
                findNavController().navigate(action)
            }
        }

        binding.toMainAddPageFromAddInfPage.setOnClickListener {
            findNavController().navigate(R.id.action_addInformationFragment_to_navigation_add)
        }
    }

    private fun getOwnAboutInformation() =
        binding.addOwnAbout.text.toString()

    private fun getPlayerPosition() =
        binding.addSpinner.selectedItem.toString()

    private fun getPlayerFirstName() =
        binding.addFirstName.text.toString()

    private fun getPlayerLastName() =
        binding.addLastName.text.toString()

    private fun getPlayerPhoneNumber() =
        binding.addPhoneNumber.text.toString()

    private fun isInformationLinesEmpty() =
        binding.addFirstName.text.toString().isEmpty()
                || binding.addLastName.text.toString().isEmpty()
                || binding.addPhoneNumber.text.toString().isEmpty()

    private fun informationLinesEmpty() {
        val firstNameLine = binding.addFirstName
        val lastNameLine = binding.addLastName
        val phoneNumberLine = binding.addPhoneNumber

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