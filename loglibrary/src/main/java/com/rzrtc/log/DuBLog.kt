package com.rzrtc.log



object DuBLog : DuBIns() {
    override fun init(duBLogConfig: DuBLogConfig) {
        duBLogImp = DuBLogFactory.createLogInstance(duBLogConfig)
    }

}