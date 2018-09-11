package k.agera.com.locationwidget.push

import cn.jpush.android.api.JPushInterface
import com.agera.hometools.bean.LocationData
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.bean.Audience
import k.agera.com.locationwidget.bean.Message
import k.agera.com.locationwidget.bean.PushMessage
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.network.Config
import k.agera.com.locationwidget.network.NetCore

/**
 * Created by Administrator on 18-8-20.
 */
class PushImp : PushInter {


    var ctx = MyApp.instance()

    companion object {
        private var instance = PushImp()
        fun instance() = instance
    }

    override fun setPushAccount(alias: String) {
        MyApp.instance()?.let {
            JPushInterface.setAlias(it.baseContext, 0, alias)
            it.setSelf(alias)
        }
    }

    override fun requireLocationByAlias(alisa: String) {
        var msgJson = MyApp.instance().gson.toJson(makeMessageModule(alisa, Config.MESSAGE_LOCATION, alisa))
        TaskDriver.instance().execute(Runnable {
            NetCore.instance().doPost(Config.mPushUrl, Config.PushHeaders, msgJson)
        })

    }


    override fun resumeService() {
        if (JPushInterface.isPushStopped(ctx)) {
            JPushInterface.resumePush(ctx)
        }
    }

    override fun makeMessageModule(to: String, type: String, msg: String): PushMessage = PushMessage(Audience(arrayOf(to)), Message(msg, type, MyApp.instance().getSelf()))

    override fun replyLocationtoAlias(alias: String, location: LocationData) {
        var msgJson = MyApp.instance().gson.toJson(makeMessageModule(alias, Config.MESSAGE_REPLY_LOCATION, MyApp.instance().gson.toJson(location)))
        TaskDriver.instance().execute(Runnable {
            NetCore.instance().doPost(Config.mPushUrl, Config.PushHeaders, msgJson)
        })
    }
}