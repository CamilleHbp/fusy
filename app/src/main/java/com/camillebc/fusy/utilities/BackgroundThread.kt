package com.camillebc.fusy.utilities

import APP_TAG
import android.os.Handler
import android.os.HandlerThread
import android.util.Log

private const val TAG = APP_TAG + "BackgroundThread"

class BackgroundThread {
        private var backgroundThread: HandlerThread? = null
        var backgroundHandler: Handler? = null

        /**
         * Starts a background thread and its [Handler].
         */
        fun startBackgroundThread() {
            backgroundThread = HandlerThread("CameraBackground").also { it.start() }
            backgroundHandler = Handler(backgroundThread?.looper)
        }

        /**
         * Stops the background thread and its [Handler].
         */
        fun stopBackgroundThread() {
            backgroundThread?.quitSafely()
            try {
                backgroundThread?.join()
                backgroundThread = null
                backgroundHandler = null
            } catch (e: InterruptedException) {
                Log.e(TAG, e.toString())
            }
        }
}