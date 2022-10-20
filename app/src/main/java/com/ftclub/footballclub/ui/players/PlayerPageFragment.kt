package com.ftclub.footballclub.ui.players

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.databinding.FragmentPlayerPageBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerPageFragment : Fragment() {

    private lateinit var _binding: FragmentPlayerPageBinding
    private val binding get() = _binding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerPageBinding.inflate(inflater, container, false)

        return binding.root
    }

}