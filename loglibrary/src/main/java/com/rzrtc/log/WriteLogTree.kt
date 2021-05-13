package com.rzrtc.log

import com.tencent.mars.xlog.XLogWrapper
import com.tencent.mars.xlog.Xlog

class WriteLogTree(duBLogConfig: DuBLogConfig) : Timber.DebugTree() {

    init {
        try {
            System.loadLibrary("c++_shared");
            System.loadLibrary("marsxlog");
        } catch (e: Exception) {

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
        XLogWrapper.openLogInstance(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, duBlogConfig.cachePath, duBlogConfig.logPath, duBlogConfig.namePreFix, duBlogConfig.cacheDays);
        XLogWrapper.getLogInstance(duBlogConfig.namePreFix).setMaxFileSize(duBlogConfig.maxFileSize)
    }

    fun appenderClose() {
        XLogWrapper.appenderClose()
    }

    fun appenderFlush() {
        XLogWrapper.appenderFlush()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        XLogWrapper.getLogInstance(logInstancePreFix).log(priority, tag, message, t)
        XLogWrapper.getLogInstance(logInstancePreFix).appenderFlush()
    }

}