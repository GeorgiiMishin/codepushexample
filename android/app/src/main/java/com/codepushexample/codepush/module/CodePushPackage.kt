package com.codepushexample.codepush.module

import android.app.DownloadManager
import android.content.Context
import android.view.View
import com.codepushexample.codepush.CodePushManager
import com.codepushexample.codepush.DiskManager
import com.codepushexample.codepush.CodePushDownloadManager
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class CodePushPackage(
    private val diskManager: DiskManager,
    private val codePushManager: CodePushManager,
    private val codePushDownloadManager: CodePushDownloadManager
) :
    ReactPackage {
    override fun createNativeModules(context: ReactApplicationContext): MutableList<NativeModule> =
        mutableListOf(
            CodePushModule(
                context,
                diskManager,
                codePushManager,
                codePushDownloadManager
            )
        )


    override fun createViewManagers(p0: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> =
        mutableListOf()
}