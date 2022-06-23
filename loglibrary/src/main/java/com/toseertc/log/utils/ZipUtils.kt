package com.toseertc.log.utils

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipUtils {

    private const val BUFFER_LEN = 8192
    /**
     * Zip the files.
     *
     * @param srcFiles The source of files.
     * @param zipFile  The ZIP file.
     * @param comment  The comment.
     * @return `true`: success<br></br>`false`: fail
     * @throws IOException if an I/O error has occurred
     */
    @Throws(IOException::class)
    fun zipFiles(srcFiles: Collection<File>?,
                 zipFile: File,
                 comment: String): Boolean {
        if (srcFiles == null || zipFile == null) return false
        var zos: ZipOutputStream? = null
        return try {

            zos = ZipOutputStream(FileOutputStream(zipFile))
            for (srcFile in srcFiles) {
                if (!zipFile(srcFile, "", zos, comment)) return false
            }
            true
        } finally {
            if (zos != null) {
                zos.finish()
                zos.close()
            }
        }
    }

    @Throws(IOException::class)
    private fun zipFile(srcFile: File,
                        rootPath: String,
                        zos: ZipOutputStream,
                        comment: String): Boolean {
        var rootPath = rootPath
        rootPath = rootPath + (if (isSpace(rootPath)) "" else File.separator) + srcFile.name
        if (srcFile.isDirectory) {
            val fileList = srcFile.listFiles()
            if (fileList == null || fileList.size <= 0) {
                val entry = ZipEntry("$rootPath/")
                entry.comment = comment
                zos.putNextEntry(entry)
                zos.closeEntry()
            } else {
                for (file in fileList) {
                    if (!zipFile(file, rootPath, zos, comment)) return false
                }
            }
        } else {
            var inputStream: InputStream? = null
            try {
                inputStream = BufferedInputStream(FileInputStream(srcFile))
                val entry = ZipEntry(rootPath)
                entry.comment = comment
                zos.putNextEntry(entry)
                val buffer = ByteArray(BUFFER_LEN)
                var len: Int
                while (inputStream.read(buffer, 0, BUFFER_LEN).also { len = it } != -1) {
                    zos.write(buffer, 0, len)
                }
                zos.closeEntry()
            } finally {
                inputStream?.close()
            }
        }
        return true
    }

    /**
     * Return whether the string is null or white space.
     *
     * @param s The string.
     * @return `true`: yes<br></br> `false`: no
     */
    fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }
}