package k.agera.com.locationwidget

import android.app.Application
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.google.gson.Gson
import k.agera.com.locationwidget.location.MapActivity
import k.agera.com.locationwidget.utils.CommonUtils
import k.agera.com.locationwidget.utils.Constants

/**
 * Created by Agera on 2018/6/13.
 */
class MyApp : Application() {



    private var selfAlias: String = ""
    var mMapActivity: MapActivity? = null
    var gson = Gson()

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

        //for testing
        var startTime = 0L
        var endTime = 0L
    }

    fun getSelf(): String {
        if (!TextUtils.isEmpty(selfAlias)) {
            return selfAlias
        } else {
            return CommonUtils.instance().getData(Constants.USERNAME, "").toString()
        }
    }

    fun setSelf(alias: String) {
        selfAlias = alias
    }

}