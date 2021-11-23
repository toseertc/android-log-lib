package com.rzrtclog.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.rzrtc.log.DuBLog
import com.rzrtc.log.interfaces.UpLoadStrategy
import com.rzrtc.log.utils.ZipUtils
import com.rzrtclog.sample.httplogger.RequestInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import javax.security.auth.login.LoginException

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenResumed {
//            while(isActive){
//                DuBLog.v("vvvvvv这是一条测试log")
//                DuBLog.d("dddddd这是一条测试\nasdasdasdlog")
//                DuBLog.w("wwwwww这是一条测试\r" +
//                        "asdasdasdlog")
//                DuBLog.e("eeeeee这是一条\n" +
//                        "asdasdasdlog测试log\n" +
//                        "asdasdasdlog\n" +
//                        "asdasdasdlog\n" +
//                        "asdasdasdlog")
//                delay(1500)
//                DuBLog.appenderFlush(false)
//            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            delay(5000)
            //5s后开始上传
            DuBLog.uploadLog(2,object :UpLoadStrategy{
                override fun upLoadLogZipFiles(zipFile: File) {
                    val files = mutableListOf<File>()
                    val rootZipFile = File("${zipFile.parent}/all-XXXX.zip")

                    files.add(zipFile)
                    ZipUtils.zipFiles(files,rootZipFile,"123");
                    OkHttpUpLoadFileUtils.postFile("https://data-center-dev.duobeiyun.com/tosee/v1/development/upload?deviceId=TSH36RCAG6D",rootZipFile,rootZipFile.name,object :OkHttpUpLoadFileUtils.ResultCallback<String>{
                        override fun onSuccess(data: String) {
                            Log.e(TAG, "onSuccess: $data" )
                        }

                        override fun onFail(e: Exception) {
                            Log.e(TAG, "onFail: $e" )
                            e.printStackTrace()

                        }

                    })
                }
            })


        }
    }


    //show http log use
    override fun onResume() {
        super.onResume()
        try {

//            val url = "https://httpbin.org/post  "
//            var jsonObject = JSONObject()
//            jsonObject.put("name","testName")
//            jsonObject.put("pwd","1234567")
//            jsonObject.put("age","23")
//            val body: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(),jsonObject.toString())
//            val request = Request.Builder()
//                .url(url)
//                .method("POST", body)
//                .build()
//            val httpBuilder = OkHttpClient.Builder()
//            val okHttpClient = httpBuilder
//                .addInterceptor(RequestInterceptor(DuBLog))  //add log interceptor
//                .build()
//            okHttpClient.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    e.printStackTrace()
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.code==200) {
//                        // do yourself
//                    }
//                }
//
//            })


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}