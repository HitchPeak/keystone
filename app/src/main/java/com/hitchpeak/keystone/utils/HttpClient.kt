package com.hitchpeak.keystone.utils

import android.content.Context


class HttpClient constructor(context: Context)  {

    companion object {

        @Volatile
        var instance : HttpClient? = null
            private set

        fun init(context: Context) {
            synchronized(this) {
                instance = HttpClient(context)
            }
        }

    }

}