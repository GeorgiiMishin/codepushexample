package com.codepushexample.codepush

import com.codepushexample.BuildConfig
import java.io.File

private fun higherVersion(first: String, second: String): String {
    val firstArray = first.split(".").mapNotNull { it.toIntOrNull() }
    val secondArray = second.split(".").mapNotNull { it.toIntOrNull() }

    firstArray.forEachIndexed { index, version ->
        val secondVersion = secondArray.getOrNull(index) ?: return first
        if (version > secondVersion) return first
        if (secondVersion > version) return second
    }

    return first
}

private fun makeVersionsComparator(): Comparator<String> = object : Comparator<String> {
    override fun compare(version1: String?, version2: String?): Int {
        (version1 != null && version2 != null && version1 != version2) ?: return 0
        version1 ?: return -1
        version2 ?: return 1

        val higher = higherVersion(version1, version2)

        if (higher == version1) {
            return 1
        }

        return -1
    }
}

class CodePushManager(private val diskManager: DiskManager) {
    val version: String
        get() {
            val currentAppVersion = BuildConfig.VERSION_NAME
            val codePushVersions = diskManager.allCodePushVersions

            codePushVersions.isNotEmpty() ?: return currentAppVersion

            val maxCodePushVersion = codePushVersions.maxOfWithOrNull(makeVersionsComparator()) {
                it
            } ?: return currentAppVersion

            return higherVersion(maxCodePushVersion, currentAppVersion)
        }

    val versionPath: String?
        get() = try {
            diskManager.getBundleForVersion(version).path + "index.android.bundle"
        } catch (ex: Throwable) {
            null
        }
}