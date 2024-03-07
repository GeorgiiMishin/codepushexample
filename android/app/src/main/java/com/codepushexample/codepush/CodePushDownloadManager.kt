package com.codepushexample.codepush

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.loader.content.AsyncTaskLoader
import java.io.File

class CodePushDownloadManager(
    private val diskManager: DiskManager,
    private val downloadManager: DownloadManager?,
    private val codePushManager: CodePushManager,
    context: Context
) : AsyncTaskLoader<File>(context) {
    private var host: String? = null
    fun setup(host: String) {
        this.host = host
    }

    override fun loadInBackground(): File? {
        val host = this.host ?: return null

        val outputFile = try {
            diskManager.getArchiveForDownload()
        } catch (ex: Throwable) {
            return null
        }

        val uri = "$host/android/${codePushManager.version}"

        val request = DownloadManager
            .Request(Uri.parse(uri))
            .setTitle("Загрузка обновления")
            .setDescription("Загрузка обновления приложения СМ")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(outputFile.toUri())

        try {
            downloadManager?.enqueue(request)
        } catch (e: Throwable) {
            return null
        }

        return outputFile
    }
}