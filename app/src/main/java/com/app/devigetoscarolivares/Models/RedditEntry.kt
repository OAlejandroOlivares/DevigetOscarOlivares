package com.app.devigetoscarolivares.Models

import android.os.Parcel
import android.os.Parcelable

data class RedditEntry(
var domain:String?,
var banned_by:String?,
var media_embed:String?,// {},
var subreddit:String?,
var selftext_html:String?,
var selftext:String?,
var likes:String?, //null,
var user_reports:String?,// [],
var secure_media:String?,
var link_flair_text:String?,
var id:String?,
var gilded:Int?,
var secure_media_embed:String?,
var clicked:Boolean?,
var report_reasons:String?,
var author:String?,
var media:String?,
var score:String?,
var approved_by:String?,
var over_18:Boolean?,
var hidden:Boolean?,
var thumbnail:String?,
var subreddit_id:String?,
var edited:Boolean?,
var link_flair_css_class:String?,
var author_flair_css_class:String?,
var downs:Int?,
var mod_reports:String?,
var saved:Boolean?,
var is_self:Boolean?,
var name:String?,
var permalink:String?,
var stickied:Boolean?,
var created:Int?,
var url:String?,
var author_flair_text:String?,
var title:String?,
var created_utc:Int?,
var ups:Int?,
var num_comments:Int?,
var visited:Boolean?,
var num_reports:Int?,
var distinguished:String?
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(domain)
        parcel.writeString(banned_by)
        parcel.writeString(media_embed)
        parcel.writeString(subreddit)
        parcel.writeString(selftext_html)
        parcel.writeString(selftext)
        parcel.writeString(likes)
        parcel.writeString(user_reports)
        parcel.writeString(secure_media)
        parcel.writeString(link_flair_text)
        parcel.writeString(id)
        parcel.writeValue(gilded)
        parcel.writeString(secure_media_embed)
        parcel.writeValue(clicked)
        parcel.writeString(report_reasons)
        parcel.writeString(author)
        parcel.writeString(media)
        parcel.writeString(score)
        parcel.writeString(approved_by)
        parcel.writeValue(over_18)
        parcel.writeValue(hidden)
        parcel.writeString(thumbnail)
        parcel.writeString(subreddit_id)
        parcel.writeValue(edited)
        parcel.writeString(link_flair_css_class)
        parcel.writeString(author_flair_css_class)
        parcel.writeValue(downs)
        parcel.writeString(mod_reports)
        parcel.writeValue(saved)
        parcel.writeValue(is_self)
        parcel.writeString(name)
        parcel.writeString(permalink)
        parcel.writeValue(stickied)
        parcel.writeValue(created)
        parcel.writeString(url)
        parcel.writeString(author_flair_text)
        parcel.writeString(title)
        parcel.writeValue(created_utc)
        parcel.writeValue(ups)
        parcel.writeValue(num_comments)
        parcel.writeValue(visited)
        parcel.writeValue(num_reports)
        parcel.writeString(distinguished)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedditEntry> {
        override fun createFromParcel(parcel: Parcel): RedditEntry {
            return RedditEntry(parcel)
        }

        override fun newArray(size: Int): Array<RedditEntry?> {
            return arrayOfNulls(size)
        }
    }

}
