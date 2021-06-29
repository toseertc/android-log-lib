package com.rzrtc.log.impl

import com.rzrtc.log.DuBLogConfig
import com.tencent.mars.xlog.XLogWrapper
import com.tencent.mars.xlog.Xlog

class WriteLogTree(duBLogConfig: DuBLogConfig) : Timber.DebugTree() {

    init {
        try {
            System.loadLibrary("c++_shared");
            System.loadLibrary("marsxlog");
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initXLog(duBLogConfig)
    }

    fun getXLogPath(): String {
        return this.logPath
    }

    private lateinit var logPath: String
    private lateinit var logInstancePreFix: String

    private fun initXLog(duBlogConfig: DuBLogConfig) {
        this.logPath = duBlogConfig.logPath
        this.logInstancePreFix = duBlogConfig.namePreFix
        if (XLogWrapper.getImpl()==null) {
            val xlog = Xlog()
            XLogWrapper.setLogImp(xlog)
        }
        XLogWrapper.setConsoleLogOpen(false)
        XLogWrapper.openLogInstance(duBlogConfig.writeLogLevel.value, Xlog.AppednerModeAsync, duBlogConfig.getCachePath(), duBlogConfig.logPath, duBlogConfig.namePreFix, duBlogConfig.getCacheDays());
        XLogWrapper.getLogInstance(duBlogConfig.namePreFix).setMaxFileSize(duBlogConfig.maxFileSize)
    }

    fun appenderClose() {
        XLogWrapper.appenderClose()
    }

    fun appenderFlush(isSync :Boolean) {
        XLogWrapper.appenderFlush(isSync)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        XLogWrapper.getLogInstance(logInstancePreFix).log(priority, tag, message, t)
//        XLogWrapper.getLogInstance(logInstancePreFix).appenderFlush()
    }

}