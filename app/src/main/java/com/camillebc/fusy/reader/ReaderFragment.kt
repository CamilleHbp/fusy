package com.camillebc.fusy.reader

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.camillebc.fusy.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReaderFragment.OnReaderFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class ReaderFragment : Fragment() {
    private var listenerReader: OnReaderFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reader, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listenerReader?.onReaderFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnReaderFragmentInteractionListener) {
            listenerReader = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSearchFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerReader = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnReaderFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onReaderFragmentInteraction(uri: Uri)
    }

}
