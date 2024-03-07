package com.codepushexample.codepush.module

import com.codepushexample.codepush.CodePushManager
import com.codepushexample.codepush.DiskManager
import com.codepushexample.codepush.CodePushDownloadManager
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CodePushModule(
    context: ReactApplicationContext,
    val diskManager: DiskManager,
    val codePushManager: CodePushManager,
    val downloadManager: CodePushDownloadManager
) : ReactContextBaseJavaModule(context), CoroutineScope {
    override fun getName(): String = "CodePushModule"

    @ReactMethod
    fun initialize(host: String) {
        downloadManager.setup(host)

        launch {
            try {
                val file = downloadManager.loadInBackground() ?: return@launch
                diskManager.unpack(file)
            } catch (ex: Throwable) {
                val x = 0;
                // Handle error
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}