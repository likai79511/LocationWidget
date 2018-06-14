package k.agera.com.locationwidget.network

import android.util.Log
import k.agera.com.locationwidget.bean.HttpRequest
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Agera on 2018/6/14.
 */
class NetUtils private constructor() {

    var timeout = 10_000

    companion object {
        private var utils = NetUtils()
        fun instance() = utils
    }

    fun executeRequest(req: HttpRequest): String? {
        try {
            var url = URL(req.url)
            var connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = if (req.method == RequestMethod.Get) "GET" else "POST"
            //add header
            req.header?.let {
                for ((k, v) in it) {
                    connection.addRequestProperty(k, v)
                }
            }
            connection.connectTimeout = timeout
            connection.readTimeout = timeout


            var responseCode = connection.responseCode


            if (HttpURLConnection.HTTP_OK == responseCode) {
                var content = InputStreamReader(connection.inputStream).readText()
                return content
            } else {
                Log.e("---", "--- executeRequest failed, responseCode:$responseCode")
                return null
            }

        } catch (e: Exception) {
            Log.e("---", "--- executeRequest appear error : ${e.message}")
            return null
        }
    }

}