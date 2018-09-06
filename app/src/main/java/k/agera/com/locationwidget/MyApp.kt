package k.agera.com.locationwidget

import android.app.Application
import cn.jpush.android.api.JPushInterface
import k.agera.com.locationwidget.location.MapActivity
import k.agera.com.locationwidget.utils.CommonUtils

/**
 * Created by Agera on 2018/6/13.
 */
class MyApp : Application() {


    var selfAlias: String = ""
    var mMapActivity: MapActivity? = null


    override fun onCreate() {
        super.onCreate()

        app = this
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

        //start daemon
        CommonUtils.instance().startDaemon()
    }

    companion object {
        private lateinit var app: MyApp
        fun instance() = app
    }
}