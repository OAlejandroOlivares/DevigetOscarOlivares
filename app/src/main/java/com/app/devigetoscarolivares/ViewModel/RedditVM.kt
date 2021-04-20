package com.app.devigetoscarolivares.ViewModel

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.app.devigetoscarolivares.Fragments.EntrListFragment
import com.app.devigetoscarolivares.Models.RedditEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class RedditVM : ViewModel() {

    private val client = OkHttpClient()
    private var entries : MutableLiveData<ArrayList<RedditEntry>> = MutableLiveData()
    var token :String? = null
    private val formBody = FormBody.Builder()
        .add("grant_type", "https://oauth.reddit.com/grants/installed_client")
        .add("device_id", UUID.randomUUID().toString())
        .build()
    private val CLIENT_ID ="WoWu_4334wTkwQ"
    private var mafter: String? = null

    fun getTopEntries(after: Boolean, fragment: EntrListFragment?) : LiveData<ArrayList<RedditEntry>> {
        var tmpentries:ArrayList<RedditEntry>? = ArrayList()

        val httpBuilder: HttpUrl.Builder = "https://oauth.reddit.com/top".toHttpUrlOrNull()!!.newBuilder()
        httpBuilder.addQueryParameter("after",mafter)
        val request = Request.Builder()
            //.url("https://www.reddit.com/api/v1/authorize?client_id=${CLIENT_ID}&response_type=token&state=RANDOM_STRING&redirect_uri=http://127.0.0.1&scope=read")
            .url(httpBuilder.build())
            .addHeader("Authorization", "bearer $token")
            .addHeader("User-agent", "devigetOscar 1.0 by /u/No_Butterscotch2578")
            .build()
        CoroutineScope(IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) { //throw IOException("Unexpected code $response")
                        Log.d("asd", response.message)
                    }

                    if (after) {
                        tmpentries = entries.value
                    }

                    val json = JSONObject(response.body!!.string())
                    val json2 = json.getJSONObject("data")
                    mafter = json2.getString("after")
                    val entriesR = json2.getJSONArray("children")
                    for (i in 0 until entriesR.length()) {
                        val item: JSONObject = entriesR[i] as JSONObject
                        val item2 = item.getJSONObject("data")
                        val entri = RedditEntry(
                            item2.getString("domain"),
                            item2.getString("banned_by"),
                            item2.getString("media_embed"),
                            item2.getString("subreddit"),
                            item2.getString("selftext_html"),
                            item2.getString("selftext"),
                            item2.getString("likes"),
                            item2.getString("user_reports"),
                            item2.getString("secure_media"),
                            item2.getString("link_flair_text"),
                            item2.getString("id"),
                            item2.getInt("gilded"),
                            item2.getString("secure_media_embed"),
                            item2.getBoolean("clicked"),
                            item2.getString("report_reasons"),
                            item2.getString("author"),
                            item2.getString("media"),
                            item2.getString("score"),
                            item2.getString("approved_by"),
                            item2.getBoolean("over_18"),
                            item2.getBoolean("hidden"),
                            item2.getString("thumbnail"),
                            item2.getString("subreddit_id"),
                            //item2.getBoolean("edited"),
                            if (item2.get("edited") is Boolean) item2.getBoolean("edited") else false,
                            item2.getString("link_flair_css_class"),
                            item2.getString("author_flair_css_class"),
                            item2.getInt("downs"),
                            item2.getString("mod_reports"),
                            item2.getBoolean("saved"),
                            item2.getBoolean("is_self"),
                            item2.getString("name"),
                            item2.getString("permalink"),
                            item2.getBoolean("stickied"),
                            item2.getInt("created"),
                            item2.getString("url"),
                            item2.getString("author_flair_text"),
                            item2.getString("title"),
                            item2.getInt("created_utc"),
                            item2.getInt("ups"),
                            item2.getInt("num_comments"),
                            item2.getBoolean("visited"),
                            if (item2.isNull("num_reports")) null else item2.getInt("num_reports"),
                            item2.getString("distinguished")
                        )
                        tmpentries!!.add(entri)
                    }
                    withContext(Main) {
                        entries.value = tmpentries
                    }
                }
            }catch (e :Exception){
                withContext(Main){
                    fragment?.displayNoInternetConnection()
                }
            }
        }

        return entries
    }

    fun authReddit(): Boolean {

        val request = Request.Builder()
            //.url("https://www.reddit.com/api/v1/authorize?client_id=${CLIENT_ID}&response_type=token&state=RANDOM_STRING&redirect_uri=http://127.0.0.1&scope=read")
            .url("https://www.reddit.com/api/v1/access_token")
            .post(formBody)
            .addHeader("Authorization", Credentials.basic(CLIENT_ID, ""))
            .build()
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) { //throw IOException("Unexpected code $response")
                    return false
                }
                val a = response.body!!.string()
                val json = JSONObject(a)
                token = json.getString("access_token")
                return true
            }
        }catch (e : Exception){
            Log.d("asd", e.message.toString())
            return false
        }
    }

    companion object{

        private lateinit var vmInstance : RedditVM

        @MainThread
        fun getVmInstance(owner: ViewModelStoreOwner) : RedditVM{
            if(!this::vmInstance.isInitialized){
                vmInstance = ViewModelProvider(owner).get(RedditVM::class.java)
            }
            return vmInstance
        }
    }

}