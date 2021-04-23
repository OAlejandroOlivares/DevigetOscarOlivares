package com.app.devigetoscarolivares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.constraintlayout.widget.Guideline
import androidx.drawerlayout.widget.DrawerLayout
import com.app.devigetoscarolivares.Fragments.DetailFragment
import com.app.devigetoscarolivares.Fragments.EntrListFragment
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.ViewModel.RedditVM
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity(), EntrListFragment.EntrListFragmentInterface {
    private lateinit var entryListFragment: EntrListFragment
    private lateinit var detailFragment: DetailFragment
    private lateinit var viewmodel: RedditVM
    private var guideline : Guideline? = null
    private lateinit var drawerLayout : DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        guideline = findViewById(R.id.separador)

        if(savedInstanceState== null) {
            setupViewModel()
            setUpDetailFragment()
        }else{
            viewmodel = RedditVM.getVmInstance(this)
            viewmodel.token = savedInstanceState.getString("token")
            entryListFragment = supportFragmentManager.findFragmentById(R.id.entry_list) as EntrListFragment
            detailFragment = supportFragmentManager.findFragmentById(R.id.entry_detail) as DetailFragment
            entryListFragment.setListener(this)
        }

        if(guideline== null){
            drawerLayout = findViewById(R.id.drawerLayout)
            drawerLayout.openDrawer(Gravity.LEFT)
        }


    }

    private fun setUpListFragment() {
        entryListFragment = EntrListFragment.newInstance()
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
            }else{
                withContext(Main){
                    noconnectedToInternet()
                }
            }
        }
    }

    private fun noconnectedToInternet() {
        detailFragment.displayNoInternetConnection()
    }

    override fun displayData(redditEntry: RedditEntry) {
        detailFragment.displayData(redditEntry)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        if (guideline != null){
            menu!!.findItem(R.id.showList).setVisible(false)
        }else{
            menu!!.findItem(R.id.showList).setVisible(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dismiss -> {
                dismissallRv()
                true
            }
            R.id.showList->{
                drawerLayout?.let{
                    if(it.isDrawerOpen(Gravity.LEFT)){
                        it.closeDrawer(Gravity.LEFT)
                    }else{
                        it.openDrawer(Gravity.LEFT)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dismissallRv() {
        if(this::entryListFragment.isInitialized) {
            entryListFragment.dismissAllItems()
        }else{
            Toast.makeText(applicationContext,"Nothing to dismiss",Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("token",viewmodel.token)
    }


}