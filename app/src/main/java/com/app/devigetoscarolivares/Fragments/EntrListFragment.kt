package com.app.devigetoscarolivares.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.devigetoscarolivares.Adapters.EntriesAdapter
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.R
import com.app.devigetoscarolivares.ViewModel.RedditVM

class EntrListFragment : Fragment(), EntriesAdapter.EntriesAdapterListener {

    private lateinit var listener: EntrListFragmentInterface
    private lateinit var rv :RecyclerView
    private var entries = ArrayList<RedditEntry>()
    private var entriesAdapter = EntriesAdapter(entries,this)
    private var viewmodel = RedditVM.getVmInstance(this)
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 5
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var viewManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    private lateinit var connection : TextView

    companion object{
        fun newInstance() : EntrListFragment{
            return EntrListFragment()
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
        if (savedInstanceState!= null){
            entries = savedInstanceState.getParcelableArrayList("entries")!!
        }
        rv = rootView.findViewById(R.id.rv_entries)
        connection = rootView.findViewById(R.id.connection)
        rv.layoutManager = viewManager
        rv.adapter= entriesAdapter
        swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh)
        setupScrollListener()
        swipeRefreshLayout.setOnRefreshListener {
            entriesAdapter.entries = ArrayList()
            entriesAdapter.notifyDataSetChanged()
            setupObserver(false)
        }

        if (entries.isEmpty()) {
            setupObserver(false)
        }else{
            entriesAdapter.entries = entries
            entriesAdapter.notifyDataSetChanged()
        }

        return rootView
    }

    private fun setupScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = viewManager.itemCount
                //last visible item position
                lastVisibleItem = viewManager.findLastCompletelyVisibleItemPosition()
                if (!swipeRefreshLayout.isRefreshing && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    setupObserver(true)
                }
            }
        }
        rv.addOnScrollListener(scrollListener)
    }


    private  fun setupObserver(refresh: Boolean) {
        swipeRefreshLayout.isRefreshing = true
        viewmodel.getTopEntries(refresh,this).observe(this, { entries->
            swipeRefreshLayout.isRefreshing = false
            entriesAdapter.entries = entries
            if (refresh) {
                entriesAdapter.notifyDataSetChanged()
            }else{
                entriesAdapter.notifyItemRangeChanged(0,entries.count())
            }
        })
    }

    fun setListener(mlistener : EntrListFragmentInterface){
        listener = mlistener
    }

    override fun onItemClick(redditEntry: RedditEntry) {
        listener.displayData(redditEntry)
    }

    fun dismissAllItems() {
        for (i in 0 until entriesAdapter.itemCount){
            entriesAdapter.entries.removeAt(0)
            entriesAdapter.notifyItemRemoved(0)
            entriesAdapter.notifyItemRangeChanged(0,entriesAdapter.itemCount)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("entries",entriesAdapter.entries)
    }

    fun displayNoInternetConnection() {
        Toast.makeText(context,"No internet connection try again", Toast.LENGTH_LONG).show()
    }


}
