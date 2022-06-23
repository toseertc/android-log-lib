package com.toseertc.log.interfaces

import java.io.File

/**
 * 上传策略
 */
interface UpLoadStrategy {
    fun upLoadLogZipFiles(zipFile: File)
}