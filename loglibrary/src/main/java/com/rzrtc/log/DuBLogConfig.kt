package com.rzrtc.log

import android.content.Context

class DuBLogConfig (val context: Context){
    var logPath: String = "${context.getExternalFilesDir("log")}/DubLogs"
    //cachePath这个参数必传，而且要data下的私有文件目录，例如 /data/data/packagename/files/xlog， mmap文件会放在这个目录，如果传空串，可能会发生 SIGBUS 的crash。
    private var cachePath =  "${context.cacheDir}/xLog"  //官方建议给应用的 /data/data/packname/files/log 目录。否则有可能被日志的自动清理功能清理掉
    var maxFileSize: Long = 0L
    var namePreFix: String = "DubLog"
    var debugMode:Boolean = true
    private var cacheDays: Int = 0 //cacheDays 的意思是 在多少天以后 从缓存目录移到日志目录


    fun setDebugMode(debugMode: Boolean): DuBLogConfig {
        this.debugMode = debugMode
        return this
    }

    fun getCachePath():String{
        return cachePath
    }

    fun getCacheDays():Int{
        return cacheDays
    }
    fun setLogPath(logPath: String): DuBLogConfig {
        this.logPath = logPath
        return this
    }

    fun setMaxFileSize(maxFileSize: Long): DuBLogConfig {
        this.maxFileSize = maxFileSize
        return this
    }

    fun setNamePreFix(namePreFix: String): DuBLogConfig {
        this.namePreFix = namePreFix
        return this
    }

    inline fun create(func: DuBLogConfig.() -> Unit): DuBLogConfig {
        func()
        return this
    }
}