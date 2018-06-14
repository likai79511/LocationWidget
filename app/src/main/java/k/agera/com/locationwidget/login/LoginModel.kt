package k.agera.com.locationwidget.login

import android.text.TextUtils
import android.util.Log
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.network.NetUtils
import k.agera.com.locationwidget.network.Restful

/**
 * Created by Agera on 2018/6/14.
 */
class LoginModel private constructor() {


    companion object {

        private lateinit var mCallback: LoginCallback
        private val model = LoginModel()

        fun instance(callback: LoginCallback): LoginModel {
            mCallback = callback
            return model
        }
    }


    fun checkTel(tel: String?) = !TextUtils.isEmpty(tel) && tel!!.length == 11

    fun checkPassword(password: String?) = !TextUtils.isEmpty(password) && password!!.length > 6


    fun login(tel: String, password: String) {
        TaskDriver.instance().execute(Runnable {
            var content = NetUtils.instance().executeRequest(Restful.instance().login(tel, password))
            if (TextUtils.isEmpty(content)) {
                mCallback.onError()
                return@Runnable
            }

            Log.e("---", "--result:$content")

        })
    }

}