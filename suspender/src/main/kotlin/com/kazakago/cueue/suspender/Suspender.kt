package com.kazakago.cueue.suspender

import com.typesafe.config.ConfigFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.EMPTY_REQUEST
import picocli.CommandLine
import picocli.CommandLine.Option
import java.util.concurrent.Callable
import kotlin.system.exitProcess

fun main(args: Array<String>): Unit = exitProcess(
    CommandLine(Suspender()).execute(*args)
)

class Suspender : Callable<Int> {

    @Option(names = ["-mode"], required = true)
    lateinit var mode: String

    override fun call(): Int {
        val conf = ConfigFactory.load()
        val accessToken = conf.getString("render.token")
        val serviceIds = conf.getStringList("render.service.ids")
        serviceIds.forEach { request(accessToken, it) }
        return 0
    }

    private fun request(accessToken: String, serviceId: String) {
        val url = when (mode) {
            "off" -> "https://api.render.com/v1/services/$serviceId/suspend"
            "on" -> "https://api.render.com/v1/services/$serviceId/resume"
            else -> throw IllegalArgumentException()
        }
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(EMPTY_REQUEST)
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IllegalStateException()
    }
}
