package com.rzrtc.log.impl

import android.util.Log
import com.rzrtc.log.BuildConfig
import com.rzrtc.log.DuBLogConfig
import com.rzrtc.log.Timber
import com.rzrtc.log.WriteLogTree
import com.rzrtc.log.interfaces.DuBLogInterface
import com.rzrtc.log.logger.AndroidLogAdapter
import com.rzrtc.log.logger.FormatStrategy
import com.rzrtc.log.logger.Logger
import com.rzrtc.log.logger.PrettyFormatStrategy
import com.rzrtc.log.utils.ZipUtils

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DuBLogImp() : DuBLogInterface {

    lateinit var duBLogConfig: DuBLogConfig
    var writeLogTree: WriteLogTree? = null

    private lateinit var timber: Timber;

    constructor(duBLogConfig: DuBLogConfig) : this() {
        this.duBLogConfig = duBLogConfig
        init(duBLogConfig)
    }

    fun init(duBLogConfig: DuBLogConfig) { //todo 多次调用
        writeLogTree = WriteLogTree(duBLogConfig)
        timber = Timber()
        if (BuildConfig.DEBUG) {
            val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(duBLogConfig.showThreadInfoInDebugMode) // (Optional) Whether to show thread info or not. Default true
                    .methodCount(0) // (Optional) How many method line to show. Default 2
                    .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
                    .tag("[${duBLogConfig.namePreFix}]") // (Optional) Global tag for every log. Default PRETTY_LOGGER
                    .build()

            var logger = Logger()
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
        timber.v("【${getInnerTag()}】 : $content")
//        LogUtils.d()
    }

    override fun v(tag: String, content: Any) {
        timber.tag(tag).v("【${getInnerTag()}】 : $content")
    }

    override fun d(content: Any) {
        timber.d("【${getInnerTag()}】 : $content")
    }

    override fun d(tag: String, content: Any) {
        timber.tag(tag).d("【${getInnerTag()}】 : $content")
    }

    override fun i(content: Any) {
        timber.i("【${getInnerTag()}】 : $content")
    }

    override fun i(tag: String, content: Any) {
        timber.tag(tag).i("【${getInnerTag()}】 : $content")
    }

    override fun w(content: Any) {
        timber.w("【${getInnerTag()}】 : $content")
    }

    override fun w(tag: String, content: Any) {
        timber.tag(tag).w("【${getInnerTag()}】 : $content")
    }

    override fun e(content: Any) {
        timber.e("【${getInnerTag()}】 : $content")
    }

    override fun e(tag: String, content: Any) {
        timber.tag(tag).e("【${getInnerTag()}】 : $content")

    }

    override fun a(content: Any) {
        timber.wtf("【${getInnerTag()}】 : $content")
    }

    override fun a(tag: String, content: Any) {
        timber.tag(tag).wtf("【${getInnerTag()}】 : $content")

    }

    private fun getInnerTag(): String? {
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        val stackTrace = Throwable().stackTrace
        val stackIndex = 3
        val targetElement = stackTrace[stackIndex]
        val fileName = getTagFileName(targetElement)

        val index = fileName?.indexOf('.') // Use proguard may not find '.'.
        var s = if (index == -1) fileName else fileName?.substring(0, index ?: 0)
        return s
    }

    private fun getTagFileName(targetElement: StackTraceElement): String? {
        val fileName = targetElement.fileName
        if (fileName != null) return fileName
        // If name of file is null, should add
        // "-keepattributes SourceFile,LineNumberTable" in proguard file.
        var className = targetElement.className
        val classNameInfo = className.split("\\.".toRegex()).toTypedArray()
        if (classNameInfo.size > 0) {
            className = classNameInfo[classNameInfo.size - 1]
        }
        val index = className.indexOf('$')
        if (index != -1) {
            className = className.substring(0, index)
        }
        return "$className.java"
    }


    override fun uploadLog() {

        //上传今天日志
        //1 获取日志路径
        val xLogPath = getLogPath()
        //2进行压缩
        Log.e("lzj", "xLogPath!! $xLogPath")

        //获取今天日期
        var format = SimpleDateFormat("YYYYMMdd").format(Date())

        var lodDir = File(xLogPath)
        if (lodDir.exists() && lodDir.isDirectory) {
            var filter = lodDir.listFiles().filter {
                it.name.contains(format) && it.name.endsWith(".xlog")
            }
            //todo zipfile加上前缀
            ZipUtils.zipFiles(filter, File("$xLogPath/${duBLogConfig.namePreFix}-$format.zip"), "${duBLogConfig.namePreFix} - $format 日志")
        }

        //3 todo 进行上传 --上传成功删除zip文件

    }

    override fun getLogPath(): String? {
        return writeLogTree?.getXLogPath()
    }

    override fun appenderClose() {
        writeLogTree?.appenderClose()
    }

    override fun appenderFlush() {
        writeLogTree?.appenderFlush()
    }


}