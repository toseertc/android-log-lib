package com.toseertc.log

import android.content.Context

class ToseeLogConfig(val context: Context) {
    var logPath: String = "${context.getExternalFilesDir("log")}/DubLogs"

    //cachePath这个参数必传，而且要data下的私有文件目录，例如 /data/data/packagename/files/xlog， mmap文件会放在这个目录，如果传空串，可能会发生 SIGBUS 的crash。
    private var cachePath =
        "${context.cacheDir}/xLog"  //官方建议给应用的 /data/data/packname/files/log 目录。否则有可能被日志的自动清理功能清理掉
    var maxFileSize: Long = 0L
    var namePreFix: String = "DubLog"
    var debugMode: Boolean = true
    var writeLogLevel = WritLevel.LEVEL_INFO
    private var cacheDays: Int = 0 //cacheDays 的意思是 在多少天以后 从缓存目录移到日志目录

    //每次启动时会删除过期文件，只保留十天内的日志文件( c文件中定义了  long max_alive_time_ = 10 * 24 * 60 * 60;    // 10 days in second )，所以给 Xlog 的目录请使用单独目录，防止误删其他文件。目前不会根据文件大小进行清理。
    //具体设置在xlog.setMaxAliveTime


    fun setDebugMode(debugMode: Boolean): ToseeLogConfig {
        this.debugMode = debugMode
        return this
    }

    fun getCachePath(): String {
        return cachePath
    }

    fun getCacheDays(): Int {
        return cacheDays
    }

    fun setLogPath(logPath: String): ToseeLogConfig {
        this.logPath = logPath
        return this
    }

    fun setWriteLogLevel(writeLogLevel: WritLevel): ToseeLogConfig {
        this.writeLogLevel = writeLogLevel
        return this
    }

    fun setMaxFileSize(maxFileSize: Long): ToseeLogConfig {
        this.maxFileSize = maxFileSize
        return this
    }

    fun setNamePreFix(namePreFix: String): ToseeLogConfig {
        this.namePreFix = namePreFix
        return this
    }

    inline fun create(func: ToseeLogConfig.() -> Unit): ToseeLogConfig {
        func()
        return this
    }


    enum class WritLevel(val value: Int) {
        LEVEL_VERBOSE(0),
        LEVEL_DEBUG(1),
        LEVEL_INFO(2),
        LEVEL_WARNING(3),
        LEVEL_ERROR(4)
    }

}