package k.agera.com.locationwidget.network

import k.agera.com.locationwidget.bean.HttpRequest

/**
 * Created by Agera on 2018/6/14.
 */
class HttpRequestFactory private constructor() {

    private var url: String = ""
    private var method: RequestMethod = RequestMethod.Get
    private var header: Map<String, String>? = null
    private var body: String? = null


    companion object {
        private var factory = HttpRequestFactory()
        fun instance() = factory
    }


    fun url(url: String): HttpRequestFactory {
        this.url = url
        return this
    }


    fun method(method: RequestMethod): HttpRequestFactory {
        this.method = method
        return this
    }

    fun header(header: Map<String, String>): HttpRequestFactory {
        this.header = header
        return this
    }

    fun body(body: String): HttpRequestFactory {
        this.body = body
        return this
    }


    fun compile(): HttpRequest {
        return HttpRequest(url, method, body, header)
    }
}