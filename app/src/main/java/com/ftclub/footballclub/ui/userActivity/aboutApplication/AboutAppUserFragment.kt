package com.ftclub.footballclub.ui.userActivity.aboutApplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftclub.footballclub.R

/**
 * A simple [Fragment] subclass.
 * Use the [AboutAppUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutAppUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app_user, container, false)
    }
}