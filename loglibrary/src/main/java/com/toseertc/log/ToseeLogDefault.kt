package com.toseertc.log



object ToseeLogDefault : ToseeIns() {
    override fun init(duBLogConfig: ToseeLogConfig) {
        duBLogImp = ToseeLogFactory.createLogInstance(duBLogConfig)
    }

}