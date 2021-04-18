package com.app.devigetoscarolivares.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.devigetoscarolivares.Models.RedditEntry
import kotlinx.coroutines.CompletableJob
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RedditVM : ViewModel() {

    private val client = OkHttpClient()
    var entries : List<RedditEntry> = ArrayList<RedditEntry>()
    var token :String? = null
    val formBody = FormBody.Builder()
        .add("grant_type", "https://oauth.reddit.com/grants/installed_client")
        .add("device_id", UUID.randomUUID().toString())
        .build()
    private val CLIENT_ID ="WoWu_4334wTkwQ"


    fun getTopEntries(after: String?) : List<RedditEntry> {
        val request = Request.Builder()
            //.url("https://www.reddit.com/api/v1/authorize?client_id=${CLIENT_ID}&response_type=token&state=RANDOM_STRING&redirect_uri=http://127.0.0.1&scope=read")
            .url("https://oauth.reddit.com/top")
            .addHeader("Authorization","bearer ${token}")
            .addHeader("User-agent","devigetOscar 1.0 by /u/No_Butterscotch2578")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) { //throw IOException("Unexpected code $response")
                Log.d("asd",response.message)
            }
            var json = JSONObject(response.body!!.string())
            token = json.getString("access_token")
        }
        return entries
    }

    fun authReddit(authJob: CompletableJob) {
        val request = Request.Builder()
            //.url("https://www.reddit.com/api/v1/authorize?client_id=${CLIENT_ID}&response_type=token&state=RANDOM_STRING&redirect_uri=http://127.0.0.1&scope=read")
            .url("https://www.reddit.com/api/v1/access_token")
            .post(formBody)
            .addHeader("Authorization",Credentials.basic(CLIENT_ID,""))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) { //throw IOException("Unexpected code $response")
                authJob.completeExceptionally(IOException("Unexpected code $response"))
            }
            var json = JSONObject(response.body!!.string())
            token = json.getString("access_token")
            authJob.complete()
        }
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