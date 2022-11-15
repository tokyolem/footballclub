package com.ftclub.footballclub.ui.administratorActivity.players.playerPageP

import android.content.ClipData
import android.content.Context
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
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayerPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerPageFragment : Fragment() {

    private var _binding: FragmentPlayerPageBinding? = null
    private val binding get() = _binding!!

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
        dbViewModel.getAccountById(args.accountId)

        dbViewModel.accountById.observe(viewLifecycleOwner) { account ->
            account?.let {
                binding.email.text = account.accountEmail
                verifyForInformationEmpty(account)
                navigation(account)
            }
        }

        copyPhoneNumber()
    }

    private fun navigation(account: Accounts) {
        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_playerPageFragment_to_navigation_players)
        }

        binding.edit.setOnClickListener {
            findNavController().navigate(
                PlayerPageFragmentDirections.actionPlayerPageFragmentToPlayerEditFragment(
                    account.id!!.toInt()
                )
            )
        }
    }

    private fun setVisibilityBottomNavView(visibility: Int) {
        val navigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.visibility = visibility
    }

    private fun verifyForInformationEmpty(account: Accounts) {
        if (account.firstName.isEmpty()) {
            binding.FI.text = "<Не указано>"
            binding.position.text = "<Не указано>"
            binding.birthday.text = "<Не указано>"
            binding.phoneNumber.text = "<Не указано>"
        } else {
            binding.FI.text = "${account.firstName} ${account.lastName}"
            binding.position.text = account.playerPosition
            binding.birthday.text = account.playerAge
            binding.phoneNumber.text = account.phoneNumber
        }

        if (account.playerInformation.isEmpty()) binding.playerInformation.text =
            "Пользователь не указал информацию о себе"
        else binding.playerInformation.text = account.playerInformation
    }

    private fun copyPhoneNumber() {
        binding.phoneCopy.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("", binding.phoneNumber.text.toString())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}