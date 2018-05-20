package com.hitchpeak.keystone.utils

import com.hitchpeak.keystone.BuildConfig
import org.springframework.web.client.RestTemplate
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object HttpClient : RestTemplate() {

    const val BASE_URL = BuildConfig.BASE_URL
    const val GET = "/get"
    const val LOCATION_SHARE_POST = "/post"

    private val mTaskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>()

    private val mThreadPool = ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            1,
            TimeUnit.SECONDS,
            mTaskQueue)

    fun execute(task: Runnable) {
        mThreadPool.execute(task)
    }
}