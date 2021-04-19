package com.app.devigetoscarolivares.Fragments

import android.icu.text.CaseMap
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.R
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private lateinit var title: TextView
    private lateinit var author : TextView
    private lateinit var imagen : ImageView

    companion object{
        fun newInstance(): DetailFragment {
            val f = DetailFragment()
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.detail_layout,container,false)
        title = rootView.findViewById<TextView>(R.id.title)
        author = rootView.findViewById<TextView>(R.id.author)
        imagen = rootView.findViewById<ImageView>(R.id.imagen)
        return rootView
    }

    fun displayData(redditEntry: RedditEntry) {
        title.text = redditEntry.title
        author.text = redditEntry.author
        Picasso.get().load(redditEntry.thumbnail).into(imagen)
    }

}
