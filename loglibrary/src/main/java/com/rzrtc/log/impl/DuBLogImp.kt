package com.rzrtc.log.impl

import com.rzrtc.log.DuBLogConfig
import com.rzrtc.log.interfaces.DuBLogInterface
import com.rzrtc.log.interfaces.UpLoadStrategy
import com.rzrtc.log.logger.AndroidLogAdapter
import com.rzrtc.log.logger.FormatStrategy
import com.rzrtc.log.logger.Logger
import com.rzrtc.log.logger.PrettyFormatStrategy
import com.rzrtc.log.utils.DateUtils
import com.rzrtc.log.utils.ZipUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class DuBLogImp(private val duBLogConfig: DuBLogConfig) : DuBLogInterface {

    var writeLogTree: WriteLogTree? = null

    private lateinit var timber: Timber
    private val zipFileEndName = ".zip"
    private val innerClassName = '$'
    private val logFileEndName = ".xlog"
    private val classFlitName = "\\."
    private val javaName = ".java"
    private val leftTag = "【"
    private val rightTag = "】"
    private val contentCombineChar = " : "


    init {
        initLogImp(duBLogConfig)
    }

    private fun initLogImp(duBLogConfig: DuBLogConfig) {
        writeLogTree = WriteLogTree(duBLogConfig)
        timber = Timber()
        if (duBLogConfig.debugMode) {
            val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(0) // (Optional) How many method line to show. Default 2
                .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
                .showThreadInfo(false)
                .tag("[${duBLogConfig.namePreFix}]") // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()

            val logger = Logger()
            logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return super.isLoggable(priority, tag)
                }
            })

            timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    logger.log(priority, tag, message, t)
                }
            }, writeLogTree)
        } else {
            timber.plant(writeLogTree)
        }
    }

    override fun v(content: Any) {
        timber.v("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun v(tag: String, content: Any) {
        timber.tag(tag).v("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun d(content: Any) {
        timber.d("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun d(tag: String, content: Any) {
        timber.tag(tag).d("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun i(content: Any) {
        timber.i("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun i(tag: String, content: Any) {
        timber.tag(tag).i("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun w(content: Any) {
        timber.w("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun w(tag: String, content: Any) {
        timber.tag(tag).w("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun e(content: Any) {
        timber.e("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun e(tag: String, content: Any) {
        timber.tag(tag).e("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")

    }

    override fun a(content: Any) {
        timber.wtf("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")
    }

    override fun a(tag: String, content: Any) {
        timber.tag(tag).wtf("$leftTag${getInnerTag()}$rightTag$contentCombineChar$content")

    }

    private fun getInnerTag(): String {
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        val stackTrace = Throwable().stackTrace
        val stackIndex = 3
        val targetElement = stackTrace[stackIndex]
        val fileName = getTagFileName(targetElement)

        val index = fileName.indexOf('.') // Use proguard may not find '.'.
        return if (index == -1) fileName else fileName.substring(0, index ?: 0)
    }

    private fun getTagFileName(targetElement: StackTraceElement): String {
        val fileName = targetElement.fileName
        if (fileName != null) return fileName
        // If name of file is null, should add
        // "-keepattributes SourceFile,LineNumberTable" in proguard file.
        var className = targetElement.className
        val classNameInfo = className.split(classFlitName.toRegex()).toTypedArray()
        if (classNameInfo.isNotEmpty()) {
            className = classNameInfo[classNameInfo.size - 1]
        }
        val index = className.indexOf(innerClassName)
        if (index != -1) {
            className = className.substring(0, index)
        }
        return "$className$javaName"
    }


    override fun uploadLog(logDays: Int, upLoadStrategy: UpLoadStrategy) {

        //上传今天日志
        //1 获取日志路径
        appenderFlush(true)  // 同步刷新缓存区
        val xLogPath = getLogPath() ?: return
        var logDaysTemp = logDays
        //2进行压缩
        i("xLogPath!! $xLogPath")
        //获取今天日期
        val today = SimpleDateFormat("yyyyMMdd").format(Date())
        //定义日期集合
        val logDaysName = mutableListOf<String>()
        logDaysName.add(today)
        var preday = today
        while (logDaysTemp - 1 > 0) {
            val nextPreDate = DateUtils.getPreDate(preday)
            logDaysName.add(nextPreDate)
            preday = nextPreDate
            logDaysTemp -= 1
        }

        //定义zipfile
        val zipFile = File("$xLogPath/${duBLogConfig.namePreFix}-$today$zipFileEndName")

        val logDir = File(xLogPath)
        if (logDir.exists() && logDir.isDirectory) {
            val flatMap = logDaysName.flatMap {
                logDir.listFiles()?.filter { file ->
                    file.name.contains(it) && file.name.endsWith(logFileEndName)
                } ?: emptyList()
            }

            if (flatMap.isNotEmpty()) {
                ZipUtils.zipFiles(flatMap, zipFile, "${duBLogConfig.namePreFix} - $today logFiles")

            }
        }

        upLoadStrategy.upLoadLogZipFiles(zipFile)
    }

    override fun getLogPath(): String? {
        return writeLogTree?.getXLogPath()
    }

    override fun appenderClose() {
        writeLogTree?.appenderClose()
    }

    override fun appenderFlush(isSync: Boolean) {
        writeLogTree?.appenderFlush(isSync)
    }


}