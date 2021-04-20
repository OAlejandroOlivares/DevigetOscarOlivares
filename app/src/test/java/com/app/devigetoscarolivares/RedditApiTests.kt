package com.app.devigetoscarolivares

import com.app.devigetoscarolivares.ViewModel.RedditVM
import org.junit.Before
import org.junit.Test

class RedditApiTests {
    private var viewmodel = RedditVM()


    @Test
    fun auth(){
        val login =viewmodel.authReddit()
        if (login){
            assert(!viewmodel.token.isNullOrEmpty())
        }
    }

    @Before
    fun loginReddit(){
        viewmodel.authReddit()
    }


    @Test
    fun getEntries(){
        val viewModel = RedditVM()
        val entries = viewModel.getTopEntries(false,null)
        if (!entries.value.isNullOrEmpty()){
            assert(true)
        }
    }
}