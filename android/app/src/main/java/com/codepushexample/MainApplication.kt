package com.codepushexample

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import com.codepushexample.codepush.CodePushDownloadManager
import com.codepushexample.codepush.CodePushManager
import com.codepushexample.codepush.DiskManager
import com.codepushexample.codepush.module.CodePushPackage
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.flipper.ReactNativeFlipper
import com.facebook.soloader.SoLoader

class MainApplication : Application(), ReactApplication {
    private lateinit var diskManager: DiskManager
    private lateinit var codePushManager: CodePushManager
    private lateinit var codePushDownloadManager: CodePushDownloadManager

    private var versionPath: String? = null

    override val reactNativeHost: ReactNativeHost =
        object : DefaultReactNativeHost(this) {

            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    this@MainApplication.diskManager = DiskManager(applicationContext)
                    this@MainApplication.codePushManager = CodePushManager(diskManager)
                    this@MainApplication.codePushDownloadManager = CodePushDownloadManager(
                        diskManager = diskManager,
                        downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager,
                        codePushManager = codePushManager,
                        context = applicationContext,
                    )

                    this@MainApplication.versionPath = codePushManager.versionPath
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // add(MyReactNativePackage())
                    add(
                        CodePushPackage(
                            diskManager,
                            codePushManager,
                            codePushDownloadManager,
                        )
                    )
                }

            override fun getJSBundleFile(): String? {
                val res = codePushManager.versionPath ?: super.getJSBundleFile()
                return res
            }

            override fun getJSMainModuleName(): String = "index"

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
        }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(this.applicationContext, reactNativeHost)

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            load()
        }
        ReactNativeFlipper.initializeFlipper(this, reactNativeHost.reactInstanceManager)
    }
}
