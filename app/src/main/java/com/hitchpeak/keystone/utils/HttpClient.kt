package com.hitchpeak.keystone.utils

import com.hitchpeak.keystone.BuildConfig
import org.springframework.web.client.RestTemplate
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object HttpClient : RestTemplate() {

    val testValue = "Print me this!"

    const val BASE_URL = BuildConfig.BASE_URL
    val GET = "/get"
    val POST = "/post"

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