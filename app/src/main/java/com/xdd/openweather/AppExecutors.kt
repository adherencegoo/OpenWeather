package com.xdd.openweather

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Suppress("unused")
object AppExecutors {
    class UiExecutor : Executor {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable) {
            uiHandler.post(runnable)
        }
    }

    val uiExecutor = UiExecutor()
    val diskExecutor: Executor = Executors.newSingleThreadExecutor()
    val networkExecutor: Executor = Executors.newSingleThreadExecutor()
}