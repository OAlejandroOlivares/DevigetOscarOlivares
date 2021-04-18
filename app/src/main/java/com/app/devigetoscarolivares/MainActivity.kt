package com.app.devigetoscarolivares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.*
import com.app.devigetoscarolivares.ViewModel.RedditVM
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){
    private lateinit var viewmodel: RedditVM
    private lateinit var authJob : CompletableJob


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel() {
        authJob = Job()
        authJob.invokeOnCompletion {
            if (it == null) {
                viewmodel.getTopEntries(null)
            }
        }
        viewmodel = ViewModelProvider(this).get(RedditVM::class.java)
        CoroutineScope(IO + authJob).launch {
            val a = viewmodel.authReddit(authJob)
            //val b = viewmodel.testApi()
        }
    }
}