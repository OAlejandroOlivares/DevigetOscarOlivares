package com.app.devigetoscarolivares.Utils

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.util.*


class DownloadImageHelper {

    fun downloadImage(murl: String, context: Context){

        var fileName: String = murl.substring(murl.lastIndexOf('/') + 1)
        fileName = fileName.substring(0, 1).toUpperCase(Locale.ROOT) + fileName.substring(1)
        val request = DownloadManager.Request(Uri.parse(murl))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)// Visibility of the download Notification
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
                .setTitle(fileName)// Title of the Download Notification
                .setDescription("Downloading image...")// Description of the Download Notification
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true)// Set if download is allowed on roaming network

        val downloadManager =  context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request) // enqueue puts the download request in the queue.

        // using query method to "observe" download
        var finishDownload = false
        while (!finishDownload) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            if (cursor.moveToFirst()) {
                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_FAILED-> {}
                    DownloadManager.STATUS_PAUSED->{}
                    DownloadManager.STATUS_PENDING->{}
                    DownloadManager.STATUS_RUNNING-> {}
                    DownloadManager.STATUS_SUCCESSFUL-> {
                        finishDownload = true
                        Toast.makeText(context,"Download complete",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


}