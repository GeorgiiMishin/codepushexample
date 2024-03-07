package com.codepushexample.codepush

import android.content.Context
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.zip.ZipFile
import kotlin.RuntimeException

private const val ARCHIVES_PATH = "archives"
private const val BUNDLES_PATH = "bundles"

private const val BUFFER_SIZE = 4096

class DiskManager(context: Context) {
    val archives: File? = context.getExternalFilesDir(ARCHIVES_PATH)
    val bundles: File? = context.getExternalFilesDir(BUNDLES_PATH)

    val allCodePushVersions: List<String>
        get() = bundles?.list { dir, name -> File(dir, name).isDirectory }?.toList() ?: emptyList()

    fun getArchiveForDownload(): File {
        archives ?: throw RuntimeException("Archives path not found")

        return File(archives, "archive.zip").also {
            if (it.exists()) {
                it.delete()
            }
        }
    }

    fun getBundleForVersion(version: String): URI {
        bundles ?: throw RuntimeException("Bundles path not found")

        val file = File(bundles, version)

        if (!file.exists()) {
            throw RuntimeException("Version not found")
        }

        return file.toURI()
    }

    fun unpack(file: File) {
        bundles ?: throw RuntimeException("Archives path not found")
        unzip(file, bundles)

        file.delete()
    }

    init {
        archives ?: throw RuntimeException("Archives path not found")
        bundles ?: throw RuntimeException("Bundles path not found")

        archives.makeIfNotExists()
        bundles.makeIfNotExists()

    }

    private fun File.makeIfNotExists() {
        if (!exists()) {
            mkdir()
        }
    }
}

private fun unzip(zipFile: File, destinationFolder: File): Boolean {
    try {
        ZipFile(zipFile).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val path = destinationFolder.absolutePath + File.separator + entry.name
                    if (entry.isDirectory) {
                        File(path).mkdir()
                    } else {
                        extractFile(input, path)
                    }
                }
            }
        }
    } catch (e: Throwable) {
        return false
    }
    return true
}

@Throws(IOException::class)
private fun extractFile(inputStream: InputStream, destFilePath: String) {
    if (destFilePath.contains("__")) return
    val bos = BufferedOutputStream(FileOutputStream(destFilePath))
    val bytesIn = ByteArray(BUFFER_SIZE)
    var read: Int
    while (inputStream.read(bytesIn).also { read = it } != -1) {
        bos.write(bytesIn, 0, read)
    }
    bos.close()
}
