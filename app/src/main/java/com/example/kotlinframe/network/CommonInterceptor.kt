package com.pcl.mvvm.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

class CommonInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val uft8 = Charset.forName("UTF-8")
        val responseBody: ResponseBody = response.body!!
        var rBody: String? = null
        Log.e("CommonInterceptor",chain.request().url.toString())
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            var charset = uft8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(uft8)
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                }
            }
            rBody = buffer.clone().readString(charset!!)
            Log.e("CommonInterceptor", "$rBody")
        }
        return response
    }
}