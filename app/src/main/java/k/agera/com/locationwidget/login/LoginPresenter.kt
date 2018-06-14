package k.agera.com.locationwidget.login

import android.text.TextUtils
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.network.NetUtils
import k.agera.com.locationwidget.network.Restful

/**
 * Created by Agera on 2018/6/14.
 */
class LoginPresenter {

    interface CallBack {
        fun onSuccess(content: String)
        fun onError()
    }

    companion object {
        private val presenter = LoginPresenter()
        fun instance() = presenter
    }


    fun login(tel: String, password: String,callBack:CallBack) {
        TaskDriver.instance().execute(Runnable {
            var content = NetUtils.instance().executeRequest(Restful.instance().login(tel, password))
            if (TextUtils.isEmpty(content)){
                callBack.onError()
            }else{
                callBack.onSuccess(content!!)
            }
        })
    }
}