package com.ftclub.footballclub.ui.userActivity.userPlayers.userPlayerPage

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
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentUserPlayerPageBinding

/**
 * A simple [Fragment] subclass.
 * Use the [UserPlayerPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserPlayerPageFragment : Fragment() {

    private var _binding: FragmentUserPlayerPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserPlayerPageBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        navigation()
        setPlayersContent()

        return binding.root
    }

    private fun navigation() {
        binding.back.setOnClickListener {
            findNavController().navigate(
               UserPlayerPageFragmentDirections.actionNavUserPlayerPageToNavUserPlayers()
            )
        }
    }

    private fun setPlayersContent() {
        val args: UserPlayerPageFragmentArgs by navArgs()
        dbViewModel.getAccountById(args.playerId)

        dbViewModel.accountById.observe(viewLifecycleOwner) { account ->
            account?.let {
                binding.email.text = account.accountEmail
                verifyForInformationEmpty(account)
            }
        }

        copyPhoneNumber()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}