package com.rzrtclog.sample

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request.*
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
object OkHttpUpLoadFileUtils {
    private val okHttpClient: OkHttpClient
    const val USER_AGENT = "User-Agent"
    private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    private const val SEPARATE = ";"
    private val FROM_DATA: MediaType? = "multipart/form-data".toMediaTypeOrNull()
    private const val NETWORK_READ_TIMEOUT = 30_000L
    private const val NETWORK_WRITE_TIMEOUT = 60_000L
    private const val NETWORK_CONNECT_TIMEOUT = 6_000L


    init {
        okHttpClient = OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(NETWORK_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS).readTimeout(NETWORK_READ_TIMEOUT, TimeUnit.MILLISECONDS).writeTimeout(NETWORK_WRITE_TIMEOUT, TimeUnit.MILLISECONDS).build()
    }


    fun postFile(url: String, file: File, typeName: String, callback: ResultCallback<String>) {
        val fileBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val request: Request = Builder().post(fileBody).url(url).build()
        doResult(callback, request)
    }

    private fun doResult(callback: ResultCallback<String>, request: Request) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                failCallback(callback, e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.message
                if (response.isSuccessful) {
                    val url = response.request.url.toUrl().path
                    val bodystr = response.body!!.string()
                    successCallback(callback, str, bodystr)
                } else {
                    failCallback(callback, RuntimeException(response.code.toString() + ""))
                }
            }
        })
    }

    private fun failCallback(callback: ResultCallback<String>, e: Exception) {
        callback.onFail(e)
    }

    private fun successCallback(callback: ResultCallback<String>, msg: String, s: String) {
        callback.onSuccess(s)
    }

    interface ResultCallback<T> {
        fun onSuccess(data: T)
        fun onFail(e: Exception)
    }



}