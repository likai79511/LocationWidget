package k.agera.com.locationwidget.push

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.agera.hometools.bean.LocationData
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

    override fun setPushAccount(ctx: Context, alias: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requireLocationByAlias(alisa: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requireLocationByTag(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendLocationTo(location: LocationData, to: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resumeService() {
        if (JPushInterface.isPushStopped(ctx)) {
            JPushInterface.resumePush(ctx)
        }
    }
}