package com.rzrtclog.sample

import android.app.Application
import com.toseertc.log.ToseeLogDefault
import com.toseertc.log.ToseeLogConfig

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        ToseeLogDefault.init(ToseeLogConfig(this).create {
            setLogPath("${getExternalFilesDir("logFiles")}/myLog")
            setMaxFileSize(1 * 1024 * 1024)  //1M
            setNamePreFix("myLog")
            setDebugMode(BuildConfig.DEBUG)
            setWriteLogLevel(ToseeLogConfig.WritLevel.LEVEL_DEBUG)
        })
    }
}