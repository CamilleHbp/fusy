package com.camillebc.fusy.utilities

import android.os.Build
import java.io.File
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

class ImageDownloader (
    private val imageUrl: String,
    private val file: File
): Runnable {
    override fun run() {
        val inputStream = URL(imageUrl).openStream() ?: return
        val readableByteChannel = Channels.newChannel(inputStream) ?: return
        val fileChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FileChannel.open(
                file.toPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
            ) ?: return
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
    }
}