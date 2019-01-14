package com.camillebc.fusy.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.camillebc.fusy.R
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionViewModel
import com.camillebc.fusy.utilities.APP_TAG
import kotlinx.android.synthetic.main.fragment_fiction_detail.*

private const val TAG = APP_TAG + "FictionDetailFragment"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FictionDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FictionDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FictionDetailFragment : Fragment() {
    private lateinit var fictionModel: FictionViewModel
    private lateinit var fictionAuthor: TextView
    private lateinit var fictionDescription: TextView
    private lateinit var fictionImage: ImageView
    private lateinit var fictionName: TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fictionAuthor = textView_fictionAuthor
        fictionDescription = textView_fictionDescription
        fictionImage = imageView_image
        fictionName = textView_fictionName
        val requestOptions = RequestOptions().apply{
            placeholder(R.drawable.fiction_placeholder_royalroad)
            error(R.drawable.fiction_placeholder_royalroad)
        }
        fictionModel = activity?.run {
            ViewModelProviders.of(this).get(FictionViewModel::class.java)
        } ?: throw Exception("$TAG |Invalid Activity")
//        Log.i(TAG,  "Invalid Activity for ${this@FictionDetailFragment::class.simpleName}")
        fictionModel.fiction.observe(this, Observer<Fiction> { fiction ->
            fictionAuthor.text = fiction.author
            fictionDescription.text = fiction.description
            fictionName.text = fiction.name
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(fiction.imageUrl).into(fictionImage)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fiction_detail, container, false)
        // Inflate the layout for this fragment
        return view
    }
}
