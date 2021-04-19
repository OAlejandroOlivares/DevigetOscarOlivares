package com.app.devigetoscarolivares.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.devigetoscarolivares.Models.RedditEntry
import com.app.devigetoscarolivares.R
import com.squareup.picasso.Picasso

class EntriesAdapter(var entries : List<RedditEntry>,val listener : EntriesAdapterListener) : RecyclerView.Adapter<EntriesAdapter.mViewHolder>() {


    interface EntriesAdapterListener{
        fun onItemClick(redditEntry: RedditEntry)
    }

    class mViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.image)
        val author = view.findViewById<TextView>(R.id.author)
        val title = view.findViewById<TextView>(R.id.title)
        val delete = view.findViewById<ImageView>(R.id.delete)
        val coments = view.findViewById<TextView>(R.id.comments_count)
        val ups = view.findViewById<TextView>(R.id.tv_ups)

        fun onBind(entrie : RedditEntry){
            Picasso.get()
                .load(entrie.thumbnail)
                .into(image)
            author.text = entrie.author
            title.text = entrie.title
            entrie.num_comments?.let {
                coments.text = it.toString()
            }

            entrie.ups?.let {
                ups.text = it.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntriesAdapter.mViewHolder {
        return mViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.reddit_item,parent,false))
    }

    override fun onBindViewHolder(holder: EntriesAdapter.mViewHolder, position: Int) {
        holder.onBind(entries[position])
        holder.delete.setOnClickListener{
            var tmp :ArrayList<RedditEntry> = entries as ArrayList<RedditEntry>
            tmp.removeAt(position)
            entries = tmp
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener{
            listener.onItemClick(entries[position])
        }
    }

    override fun getItemCount(): Int {
        return entries.count()
    }
}
