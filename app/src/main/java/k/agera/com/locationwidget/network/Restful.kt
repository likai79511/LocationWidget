package k.agera.com.locationwidget.network

import k.agera.com.locationwidget.bean.HttpRequest
import k.agera.com.locationwidget.utils.AppendMap

/**
 * Created by Agera on 2018/6/14.
 */
class Restful {

    private val user_url = "https://api.bmob.cn/1/classes/user_widget"
    private val applicationIdDesc = "X-Bmob-Application-Id"
    private val rest_keyDesc = "X-Bmob-REST-API-Key"

    private val applicationId = "fc50317a2f1dc14576f7c600697e6799"
    private val rest_key = "51c117839a06c75aa380eb78eb8395b7"

    private val content_type = "Content-Type"
    private val format_jason = "application/json"


    private val bombHeader = AppendMap<String, String>().put(applicationIdDesc, applicationId)
            .put(rest_keyDesc, rest_key)
            .put(content_type, format_jason)
            .compile()

    companion object {
        private val restful = Restful()
        fun instance() = restful
    }


    fun login(tel: String, password: String): HttpRequest {
        return HttpRequestFactory.instance().url("$user_url?where={\"telephone\":\"$tel\",\"password\":\"$password\"}")
                .method(RequestMethod.Get)
                .header(bombHeader)
                .compile()
    }


}