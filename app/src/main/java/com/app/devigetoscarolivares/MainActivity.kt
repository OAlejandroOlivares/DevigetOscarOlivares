package com.app.devigetoscarolivares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.Guideline
import com.app.devigetoscarolivares.Fragments.DetailFragment
import com.app.devigetoscarolivares.Fragments.EntrListFragment
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.ViewModel.RedditVM
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity(), EntrListFragment.EntrListFragmentInterface {
    private lateinit var detailFragment: DetailFragment
    private lateinit var viewmodel: RedditVM
    private lateinit var authJob : CompletableJob
    private lateinit var guideline : Guideline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        guideline = findViewById(R.id.separador)
        setupViewModel()
        setUpDetailFragment()
    }

    private fun setUpListFragment() {
        var entryListFragment: EntrListFragment = EntrListFragment.newInstance()
        entryListFragment.setListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.entry_list,entryListFragment).commit()
    }

    private fun setUpDetailFragment(){
        detailFragment = DetailFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.entry_detail,detailFragment).commit()
    }

    private fun setupViewModel() {
        viewmodel = RedditVM.getVmInstance(this)
        CoroutineScope(IO).launch {
            val oauth =  viewmodel.authReddit()
            if (oauth){
                withContext(Main){
                    setUpListFragment()
                }
            }
            //val b = viewmodel.testApi()
        }
    }

    override fun displayData(redditEntry: RedditEntry) {
        detailFragment.displayData(redditEntry)
    }


}