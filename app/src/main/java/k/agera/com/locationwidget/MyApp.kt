package k.agera.com.locationwidget

import android.app.Application
import cn.jpush.android.api.JPushInterface

/**
 * Created by Agera on 2018/6/13.
 */
class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}