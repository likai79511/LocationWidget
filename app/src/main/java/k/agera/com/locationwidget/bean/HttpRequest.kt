package k.agera.com.locationwidget.bean

import k.agera.com.locationwidget.network.RequestMethod

/**
 * Created by Agera on 2018/6/14.
 */
data class HttpRequest(var url: String,
                       var method: RequestMethod,
                       var body: String?,
                       var header: Map<String, String>?)