package com.rzrtc.log

import android.content.Context

class DuBLogConfig (val context: Context){
    var logPath: String = "${context.getExternalFilesDir("log")}/marLogs"
    var cachePath =  "${context.cacheDir}/xLog"
    var maxFileSize: Long = 0L
    var namePreFix: String = "tosee"
    var cacheDays: Int = 0  //cacheDays 的意思是 在多少天以后 从缓存目录移到日志目录
    var showThreadInfoInDebugMode:Boolean = false //debug模式下是否显示线程信息
//    var traceLevel :Int = 0 //如果您希望在不同的module中打印不同的日志, 请实现DuBIns,并且需要传一个


    fun setShowThreadInfoInDebugMode(showThreadInfoInDebugMode: Boolean): DuBLogConfig {
        this.showThreadInfoInDebugMode = showThreadInfoInDebugMode
        return this
    }

//    fun setTraceLevel(traceLevel :Int): DuBLogConfig {
//        this.traceLevel = traceLevel
//        return this
//    }

    fun setLogPath(logPath: String): DuBLogConfig {
        this.logPath = logPath
        return this
    }

    fun setCachePath(cachePath: String): DuBLogConfig {
        this.cachePath = cachePath
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

    fun setCacheDays(cacheDays: Int): DuBLogConfig {
        this.cacheDays = cacheDays
        return this
    }

    inline fun create(func: DuBLogConfig.() -> Unit): DuBLogConfig {
        func()
        return this
    }
}