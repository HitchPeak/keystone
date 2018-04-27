package com.hitchpeak.keystone.utils

import android.content.Context
import com.hitchpeak.keystone.BuildConfig
import org.springframework.web.client.RestTemplate


class HttpClient constructor(context: Context) : RestTemplate() {


    companion object {
        @Volatile
        var instance : HttpClient? = null
            private set

        val baseUrl = BuildConfig.BASE_URL
        val getEndpoint = "/get"

        fun init(context: Context) {
            instance = synchronized(this) { instance ?: HttpClient(context) }
        }

    }

}