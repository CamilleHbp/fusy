package com.camillebc.fusy.account.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.camillebc.fusy.R
import com.camillebc.fusy.account.model.Account
import kotlinx.android.synthetic.main.fragment_first_launch.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirstLaunchFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class FirstLaunchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_launch, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_firstLaunch_google.setOnClickListener { googleSignIn() }
    }

    private fun googleSignIn() {
        activity?.run {
            Account.googleSignIn(this)
        }
    }
}
