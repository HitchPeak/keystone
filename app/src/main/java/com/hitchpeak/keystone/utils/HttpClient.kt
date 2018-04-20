package com.hitchpeak.keystone.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley


class HttpClient constructor(context: Context, baseUrl: String ) {

    companion object {
        val baseUrl: String = "http://10.0.2.2:8000/"

        @Volatile
        var instance : HttpClient? = null
            private set

        fun init(context: Context, baseUrl: String = this.baseUrl) {
            synchronized(this) {
                instance = HttpClient(context, baseUrl)
            }
        }

        fun makeDefaultErrorHandler() = Response.ErrorListener{ errorMsg: VolleyError -> println("Http error: $errorMsg") }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun<T> makeRequest(request: Request<T>) {
        requestQueue.add(request)
    }
}