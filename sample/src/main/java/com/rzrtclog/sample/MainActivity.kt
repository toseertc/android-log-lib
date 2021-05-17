package com.rzrtclog.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.rzrtc.log.DuBLog
import com.rzrtclog.sample.httplogger.RequestInterceptor
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenResumed {
            while(isActive){
                DuBLog.i("这是一条测试log")
                delay(1500)
            }
        }
    }


    //show http log use
    override fun onResume() {
        super.onResume()
        try {

            val url = "https://httpbin.org/post  "
            var jsonObject = JSONObject()
            jsonObject.put("name","testName")
            jsonObject.put("pwd","1234567")
            jsonObject.put("age","23")
            val body: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(),jsonObject.toString())
            val request = Request.Builder()
                .url(url)
                .method("POST", body)
                .build()
            val httpBuilder = OkHttpClient.Builder()
            val okHttpClient = httpBuilder
                .addInterceptor(RequestInterceptor(DuBLog))  //add log interceptor
                .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code==200) {
                        // do yourself
                    }
                }

            })


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}