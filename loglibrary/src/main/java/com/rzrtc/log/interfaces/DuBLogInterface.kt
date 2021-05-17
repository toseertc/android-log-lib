package com.rzrtc.log.interfaces

interface DuBLogInterface {
    fun v(content: Any)

    fun v(tag: String, content: Any)

    fun d(content: Any)

    fun d(tag: String, content: Any)

    fun i(content: Any)

    fun i(tag: String, content: Any)

    fun w(content: Any)

    fun w(tag: String, content: Any)

    fun e(content: Any)

    fun e(tag: String, content: Any)

    fun a(content: Any)

    fun a(tag: String, content: Any)

    fun uploadLog(logDays: Int, upLoadStrategy: UpLoadStrategy)

    fun getLogPath(): String?

    fun appenderClose()

    fun appenderFlush(isSync :Boolean)
}