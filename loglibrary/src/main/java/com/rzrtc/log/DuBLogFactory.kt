package com.rzrtc.log

import com.rzrtc.log.impl.DuBLogImp
import com.rzrtc.log.interfaces.DuBLogInterface

object DuBLogFactory {
    fun createLogInstance(duBLogConfig: DuBLogConfig): DuBLogInterface {
        return DuBLogImp(duBLogConfig)
    }
}