package com.camillebc.fusy.bookshelf.view

import android.content.Context
import android.os.Bundle
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
import com.camillebc.fusy.core.APP_TAG
import com.camillebc.fusy.core.model.Fiction
import com.camillebc.fusy.core.model.FictionViewModel
import kotlinx.android.synthetic.main.fragment_fiction_detail.*
import org.sufficientlysecure.htmltextview.HtmlTextView

private const val TAG = APP_TAG + "FictionDetailFragment"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FictionDetailFragment.OnDetailFragmentInteractionListener] interface
 * to handle interaction events.
 */
class FictionDetailFragment : Fragment() {
    private lateinit var fictionModel: FictionViewModel
    private lateinit var fictionAuthor: TextView
    private lateinit var fictionDescription: HtmlTextView
    private lateinit var fictionImage: ImageView
    private lateinit var fictionName: TextView
    private lateinit var listener: OnDetailFragmentInteractionListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fictionAuthor = fictionDetail_author
        fictionDescription = textView_fictionDescription
        fictionImage = fictionDetail_image
        fictionName = fictionDetail_name
        val requestOptions = RequestOptions().apply {
            placeholder(R.drawable.fiction_placeholder_royalroad)
            error(R.drawable.fiction_placeholder_royalroad)
        }
        fictionModel = activity?.run {
            ViewModelProviders.of(this).get(FictionViewModel::class.java)
        } ?: throw Exception("$TAG | Invalid Activity")
        fictionModel.fictionDetail.observe(viewLifecycleOwner, Observer { fiction ->
            fictionAuthor.text = fiction.author
            fictionDescription.setHtml(StringBuilder().apply {
                fiction.description.map { append(it) }
            }.toString())
            fictionName.text = fiction.name
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(fiction.imageUrl).into(fictionImage)
        })
        button_fragmentFictionDetail_add.setOnClickListener { listener.onAdd(fictionModel.fictionDetail.value!!) }
        button_fragmentFictionDetail_read.setOnClickListener { listener.onRead(fictionModel.fictionDetail.value!!) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDetailFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnridFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fiction_detail, container, false)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnDetailFragmentInteractionListener {
        fun onAdd(item: Fiction)
        fun onRead(item: Fiction)
    }
}
