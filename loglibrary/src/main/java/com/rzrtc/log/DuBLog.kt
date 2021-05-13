package com.rzrtc.log



object DuBLog : DuBIns() {
    override fun init(duBLogConfig: DuBLogConfig) {
        duBLogImp = DuBLogFactory.createLogInstance(duBLogConfig)
    }

//    fun appenderFlush() {
//        duBLogImp .appenderFlush()
//    }

//    fun appenderClose() {
//        duBLogImp?.appenderClose()
//    }

//    fun uploadTodayLog() {
//        duBLogImp?.uploadTwoDayLog()
//    }


}