package com.rzrtc.log

import androidx.annotation.NonNull
import com.rzrtc.log.interfaces.DuBLogInterface

abstract class DuBIns {

    @NonNull
    abstract fun init(duBLogConfig: DuBLogConfig)

    protected var duBLogImp: DuBLogInterface? = null

    fun v(content: Any) {
        duBLogImp?.v(content)
    }

    fun v(tag: String, content: Any) {
        duBLogImp?.v(tag, content)
    }

    fun d(content: Any) {
        duBLogImp?.d(content)
    }

    fun d(tag: String, content: Any) {
        duBLogImp?.d(tag, content)
    }

    fun i(content: Any) {
        duBLogImp?.i(content)
    }

    fun i(tag: String, content: Any) {
        duBLogImp?.i(tag, content)
    }

    fun w(content: Any) {
        duBLogImp?.w(content)
    }

    fun w(tag: String, content: Any) {
        duBLogImp?.w(tag, content)
    }

    fun e(content: Any) {
        duBLogImp?.e(content)
    }

    fun e(tag: String, content: Any) {
        duBLogImp?.e(tag, content)
    }

    fun a(content: Any) {
        duBLogImp?.a(content)
    }

    fun a(tag: String, content: Any) {
        duBLogImp?.a(tag, content)
    }

    fun appenderFlush() {
        duBLogImp?.appenderFlush()
    }

    fun appenderClose() {
        duBLogImp?.appenderClose()
    }

    fun uploadLog() {
        duBLogImp?.uploadLog()
    }

}