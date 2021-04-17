package com.app.devigetoscarolivares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import com.app.devigetoscarolivares.ViewModel.RedditVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
    }

    private fun setupViewModel() {
        val viewmodel = ViewModelProvider(this).get(RedditVM::class.java)
        CoroutineScope(IO).launch {
            val a = viewmodel.authReddit()
            //val b = viewmodel.testApi()
        }
    }
}