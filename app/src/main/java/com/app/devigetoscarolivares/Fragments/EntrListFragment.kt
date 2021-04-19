package com.app.devigetoscarolivares.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.devigetoscarolivares.Adapters.EntriesAdapter
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.R
import com.app.devigetoscarolivares.ViewModel.RedditVM

class EntrListFragment : Fragment(), EntriesAdapter.EntriesAdapterListener {

    private lateinit var listener: EntrListFragment.EntrListFragmentInterface
    private lateinit var rv :RecyclerView
    private var entries = ArrayList<RedditEntry>()
    private var entriesAdapter = EntriesAdapter(entries,this)
    private var viewmodel = RedditVM.getVmInstance(this)
    companion object{
        fun newInstance() : EntrListFragment{
            val f = EntrListFragment()
            return f
        }
    }

    interface EntrListFragmentInterface{
        fun displayData(redditEntry: RedditEntry)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.entrie_list,container,false)
        rv = rootView.findViewById(R.id.rv_entries)
        rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rv.adapter= entriesAdapter
        setupObserver()
        return rootView
    }

    private  fun setupObserver() {
        viewmodel.getTopEntries(false).observe(this, Observer { entries->
            entriesAdapter.entries = entries
            entriesAdapter.notifyDataSetChanged()
        })
    }

    fun setListener(mlistener : EntrListFragmentInterface){
        listener = mlistener
    }

    override fun onItemClick(redditEntry: RedditEntry) {
        listener.displayData(redditEntry)
    }

}
