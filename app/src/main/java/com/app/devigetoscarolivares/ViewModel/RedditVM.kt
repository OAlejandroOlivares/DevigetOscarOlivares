package com.app.devigetoscarolivares.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.devigetoscarolivares.Models.RedditEntry
import okhttp3.*
import java.io.IOException
import java.util.*

class RedditVM : ViewModel() {

    private val client = OkHttpClient()
    var entries : MutableLiveData<List<RedditEntry>> = MutableLiveData()
    val formBody = FormBody.Builder()
        .add("grant_type", "https://oauth.reddit.com/grants/installed_client")
        .add("device_id", UUID.randomUUID().toString())
        .build()
    private val CLIENT_ID ="WoWu_4334wTkwQ"
    fun getTopEntries(after: String?) : LiveData<List<RedditEntry>> {
        return entries
    }

    fun authReddit() : Boolean{
        val request = Request.Builder()
            //.url("https://www.reddit.com/api/v1/authorize?client_id=${CLIENT_ID}&response_type=token&state=RANDOM_STRING&redirect_uri=http://127.0.0.1&scope=read")
            .url("https://www.reddit.com/api/v1/access_token")
            .post(formBody)
            .addHeader("Authorization",Credentials.basic(CLIENT_ID,""))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
        }
        return false
    }

    fun testApi(){
        val request = Request.Builder()
            .url("https://www.reddit.com/api/v1/r/top")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
        }
    }

}