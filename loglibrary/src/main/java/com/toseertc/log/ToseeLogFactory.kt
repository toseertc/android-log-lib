package com.toseertc.log

import com.toseertc.log.impl.ToseeLogImp
import com.toseertc.log.interfaces.ToseeLogInterface

object ToseeLogFactory {
    fun createLogInstance(duBLogConfig: ToseeLogConfig): ToseeLogInterface {
        return ToseeLogImp(duBLogConfig)
    }
}