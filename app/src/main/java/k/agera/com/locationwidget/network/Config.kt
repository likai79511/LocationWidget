package k.agera.com.locationwidget.network

import k.agera.com.locationwidget.utils.AppendMap

/**
 * Created by Agera on 2018/8/21.
 */
class Config {

    companion object {

        //Bomb config
        private val applicationId = "5ce5aef97e0a4cebec25865db694a746"
        private val api_key = "476289e636d663d0c409be844592abb9"
        private val secret_key = "3883e35644215826"
        private val master_key = "52e29946f10347276f56ea695cd56ef6"

        val BombHeaders = AppendMap<String, String>().put("X-Bmob-Application-Id", applicationId)
                .put("X-Bmob-REST-API-Key", api_key)
                .put("X-Bmob-REST-API-Key", "application/json")
                .compile()


        val userTable = "https://api.bmob.cn/1/classes/user"



    }


}