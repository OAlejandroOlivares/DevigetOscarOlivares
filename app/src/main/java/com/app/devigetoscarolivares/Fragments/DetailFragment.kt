package com.app.devigetoscarolivares.Fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.R
import com.app.devigetoscarolivares.Utils.DownloadImageHelper
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private lateinit var title: TextView
    private lateinit var author : TextView
    private lateinit var imagen : ImageView
    private var mEntry: RedditEntry? = null
    private val WRITE_PERMISSION_CODE =13
    companion object{
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.detail_layout,container,false)
        title = rootView.findViewById(R.id.title)
        author = rootView.findViewById(R.id.author)
        imagen = rootView.findViewById(R.id.imagen)
        imagen.setOnClickListener {
            if (mEntry!= null && askUserPermission()){

                DownloadImageHelper().downloadImage(mEntry!!.thumbnail!!, context!!)
            }
        }
        savedInstanceState?.let {
            mEntry = it.getParcelable("entrie")!!
            displayData(mEntry!!)
        }

        return rootView
    }

    private fun askUserPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),WRITE_PERMISSION_CODE)
            false
        }else{
            true
        }
    }

    fun displayData(redditEntry: RedditEntry) {
        title.text = redditEntry.title
        author.text = redditEntry.author
        if (redditEntry.thumbnail != null && redditEntry.thumbnail!!.contains("http")) {
            Picasso.get().load(redditEntry.thumbnail).into(imagen)
            imagen.visibility = View.VISIBLE
        }else{
            imagen.visibility = View.GONE
        }
        mEntry = redditEntry
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mEntry?.let {
            outState.putParcelable("entrie",mEntry)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            WRITE_PERMISSION_CODE->{
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context!!,"You need to give permission to download images",Toast.LENGTH_LONG).show()
                } else {
                    DownloadImageHelper().downloadImage(mEntry!!.thumbnail!!, context!!)
                }
            }
        }
    }

    fun displayNoInternetConnection() {
        author.text = "No internet connection try again later"
    }

}
