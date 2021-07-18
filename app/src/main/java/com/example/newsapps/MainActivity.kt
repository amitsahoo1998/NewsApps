package com.example.newsapps

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {
    lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=5178ef8920da40e8b82b8a0b6c407fe9"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            {
                val newsJSONArray=it.getJSONArray("data")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJSONArray.length()){
                    val newsJsonObject = newsJSONArray.getJSONObject(i)
                    val news = News( newsJsonObject.getString("title"),
                    newsJsonObject.getString("author"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("image"))
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
                Log.d("Entered","Api is Working Properly")
            },
            {
                Log.d("Error","Api is not Working Properly")
            }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}
