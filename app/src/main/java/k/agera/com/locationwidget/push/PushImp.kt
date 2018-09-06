package k.agera.com.locationwidget.push

import cn.jpush.android.api.JPushInterface
import k.agera.com.locationwidget.MyApp

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
            JPushInterface.setAlias(it.baseContext,0,alias)
            it.selfAlias = alias
        }
    }

    override fun requireLocationByAlias(alisa: String){



    }


    override fun resumeService() {
        if (JPushInterface.isPushStopped(ctx)) {
            JPushInterface.resumePush(ctx)
        }
    }
}