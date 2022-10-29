package com.ftclub.footballclub.ui.players

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayerPageBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerPageFragment : Fragment() {

    private lateinit var _binding: FragmentPlayerPageBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerPageBinding.inflate(inflater, container, false)

        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        setVisibilityBottomNavView(View.GONE)
        setAccountContent()

        return binding.root
    }


    private fun setAccountContent() {
        val args: PlayerPageFragmentArgs by navArgs()
        dbViewModel.getAccountById(args.accountId.toLong())

        dbViewModel.accountById.observe(viewLifecycleOwner) { account ->
            binding.playerEmail.text = account.accountEmail
            binding.playerRegDate.text = account.dateTime
            verifyForInformationEmpty(account)
            navigation(account)
        }

        onCardClick()
        copyPhoneNumber()
    }

    private fun navigation(account: Accounts) {
        binding.toPlayersFragment.setOnClickListener {
            findNavController().navigate(R.id.action_playerPageFragment_to_navigation_players)
        }

        binding.editInformation.setOnClickListener {
            val action =
                PlayerPageFragmentDirections.actionPlayerPageFragmentToPlayerEditFragment(
                    account.id!!.toInt()
                )
            findNavController().navigate(action)
        }
    }

    private fun setVisibilityBottomNavView(visibility: Int) {
        val navigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.visibility = visibility
    }

    private fun verifyForInformationEmpty(account: Accounts) {
        if (account.firstName.isEmpty()) {
            binding.infPlayerName.text = "<Не указано>"
            binding.playerPhone.text = "<Не указано>"
            binding.playerPosition.text = "<Не указано>"
            binding.playerFirstName.text = "<Не указано>"
            binding.playerLastName.text = "<Не указано>"
            binding.playerPositionInf.text = "<Не указано>"
            binding.playerBirthday.text = "<Не указано>"

        } else {
            binding.infPlayerName.text = "${account.firstName} ${account.lastName}"
            binding.playerPhone.text = account.phoneNumber
            binding.playerPosition.text = account.playerPosition
            binding.playerFirstName.text = account.firstName
            binding.playerLastName.text = account.lastName
            binding.playerPositionInf.text = account.playerPosition
            binding.playerBirthday.text = account.playerAge
        }

        if (account.playerInformation.isEmpty()) binding.aboutPlayer.text =
            "Пользователь не указал информацию о себе"
        else binding.aboutPlayer.text = account.playerInformation
    }

    private fun onCardClick() {
        binding.aboutPlayerCard.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                binding.playerScrollView as ViewGroup?,
                AutoTransition()
            )
            if (binding.expandablePart.visibility == View.VISIBLE) {
                binding.expandablePart.visibility = View.GONE
                binding.arrow.imageAlpha = 1
                ViewsAnimation.arrowCardUpToDownAnimation(binding.arrow, requireContext())
            } else {
                binding.expandablePart.visibility = View.VISIBLE
                binding.arrow.imageAlpha = 0
                ViewsAnimation.arrowCardDownToUpAnimation(binding.arrow, requireContext())
            }
        }
    }

    private fun copyPhoneNumber() {
        binding.phoneCopy.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("", binding.playerPhone.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                requireContext(),
                "Номер скопирован в буфер обмена!",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onStop() {
        super.onStop()
        setVisibilityBottomNavView(View.VISIBLE)
    }
}