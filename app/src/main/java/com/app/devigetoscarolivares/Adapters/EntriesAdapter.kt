package com.app.devigetoscarolivares.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class EntriesAdapter(var entries: ArrayList<RedditEntry>, private val listener: EntriesAdapterListener) : RecyclerView.Adapter<EntriesAdapter.mViewHolder>() {


    interface EntriesAdapterListener{
        fun onItemClick(redditEntry: RedditEntry)
    }

    class mViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val author = view.findViewById<TextView>(R.id.author)
        private val title = view.findViewById<TextView>(R.id.title)
        val delete: ImageView = view.findViewById<ImageView>(R.id.delete)
        private val coments = view.findViewById<TextView>(R.id.comments_count)
        private val ups = view.findViewById<TextView>(R.id.tv_ups)
        private val hours = view.findViewById<TextView>(R.id.hours)
        private val status = view.findViewById<FloatingActionButton>(R.id.status)
        fun onBind(entrie: RedditEntry){
            if (entrie.thumbnail != null && entrie.thumbnail!!.contains("http")) {
                Picasso.get()
                    .load(entrie.thumbnail)
                    .into(image)
                image.visibility = View.VISIBLE
            }else{
                image.visibility = View.GONE
            }
            author.text = entrie.author
            title.text = entrie.title
            if (entrie.visited!!){
                status.visibility = View.GONE
            }else{
                status.visibility = View.VISIBLE
            }

            entrie.num_comments?.let {
                coments.text = it.toString()
            }
            val dt = Instant.ofEpochSecond(entrie.created!!.toLong())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val now: LocalDateTime = LocalDateTime.now()
            val dif = (now.hour - dt.hour).toString()
            hours.text =  "$dif hours ago"

            entrie.ups?.let {
                ups.text = it.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        return mViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reddit_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        holder.onBind(entries[position])
        holder.delete.setOnClickListener{
            entries.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,itemCount)
        }
        holder.itemView.setOnClickListener{
            entries[position].visited = true
            notifyItemChanged(position)
            listener.onItemClick(entries[position])
        }
    }

    override fun getItemCount(): Int {
        return entries.count()
    }
}
