package com.rzrtclog.sample

import android.app.Application
import com.rzrtc.log.DuBLog
import com.rzrtc.log.DuBLogConfig

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        DuBLog.init(DuBLogConfig(this).create {
            setLogPath("${getExternalFilesDir("logFiles")}/myLog")
            setMaxFileSize(1 * 1024 * 1024)  //1M
            setNamePreFix("myLog")
            setDebugMode(BuildConfig.DEBUG)
            setWriteLogLevel(DuBLogConfig.WritLevel.LEVEL_ERROR)
        })
    }
}