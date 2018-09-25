package k.agera.com.locationwidget.network

import android.util.Log
import com.google.android.agera.Result
import com.google.android.agera.net.HttpFunctions
import com.google.android.agera.net.HttpRequests
import com.google.android.agera.net.HttpResponse
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Agera on 2018/8/21.
 */
class NetCore {

    private val timeout = 20_000


    companion object {
        private var core = NetCore()
        fun instance() = core
    }


    fun doPost(url: String, header: Map<String, String>?, body: String): Result<HttpResponse> {
        var req = HttpRequests.httpPostRequest(url)
        if (header != null && header.isNotEmpty()) {
            for ((key, value) in header) {
                req.headerField(key, value)
            }
        }
        req.body(body.toByteArray()).connectTimeoutMs(timeout).readTimeoutMs(timeout)
        return HttpFunctions.httpFunction().apply(req.compile())
    }

    fun doGet(url: String, header: Map<String, String>?): Result<HttpResponse> {
        var req = HttpRequests.httpGetRequest(url)
        if (header != null && header.isNotEmpty()) {
            for ((key, value) in header) {
                req.headerField(key, value)
            }
        }
        req.connectTimeoutMs(timeout).readTimeoutMs(timeout)
        return HttpFunctions.httpFunction().apply(req.compile())
    }

    fun doPut(url: String, header: Map<String, String>?, body: String): Result<HttpResponse> {
        var req = HttpRequests.httpPutRequest(url)
        if (header != null && header.isNotEmpty()) {
            for ((key, value) in header) {
                req.headerField(key, value)
            }
        }
        req.body(body.toByteArray()).connectTimeoutMs(timeout).readTimeoutMs(timeout)
        return HttpFunctions.httpFunction().apply(req.compile())
    }

    fun checkResourceSize(resourceUrl: String): Result<Long> {
        var result = Result.failure<Long>()
        try {
            var conn = URL(resourceUrl).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            result = Result.success(conn.getHeaderField("Content-Length").toLong())
        } catch (e: Exception) {
            Log.e("--", "--appear error:${e.message}")
        }
        return result
    }

}