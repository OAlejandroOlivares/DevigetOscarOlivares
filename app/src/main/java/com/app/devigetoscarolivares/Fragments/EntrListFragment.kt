package com.app.devigetoscarolivares.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 5
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var viewManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

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
        rv.layoutManager = viewManager
        rv.adapter= entriesAdapter
        swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh)
        setupObserver(false)
        swipeRefreshLayout.setOnRefreshListener {
            entriesAdapter.entries = ArrayList<RedditEntry>()
            entriesAdapter.notifyDataSetChanged()
            setupObserver(false)
        }

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = viewManager.itemCount
                //last visible item position
                lastVisibleItem = viewManager.findLastCompletelyVisibleItemPosition()
                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold))
                {
                    swipeRefreshLayout.isRefreshing = true
                    setupObserver(true)
                    isLoading = true
                }
            }
        }

        rv.addOnScrollListener(scrollListener)

        return rootView
    }


    private  fun setupObserver(refresh: Boolean) {
        viewmodel.getTopEntries(refresh).observe(this, Observer { entries->
            isLoading= false
            swipeRefreshLayout.isRefreshing = false
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
