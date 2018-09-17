package k.agera.com.locationwidget.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.agera.hometools.bean.LocationData
import com.amap.api.location.AMapLocation
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.location.LocationUtils
import k.agera.com.locationwidget.network.Config
import k.agera.com.locationwidget.utils.CommonUtils

/**
 * Created by Agera on 2018/9/6.
 */


/*
09-06 18:35:36.611 11769-11769/k.agera.com.locationwidget E/---: ---cn.jpush.android.EXTRA ==
09-06 18:35:36.611 11769-11769/k.agera.com.locationwidget E/---: ---cn.jpush.android.TITLE == msg
09-06 18:35:36.611 11769-11769/k.agera.com.locationwidget E/---: ---cn.jpush.android.MESSAGE == Hi,JPush
09-06 18:35:36.611 11769-11769/k.agera.com.locationwidget E/---: ---cn.jpush.android.CONTENT_TYPE == text
09-06 18:35:36.611 11769-11769/k.agera.com.locationwidget E/---: ---cn.jpush.android.APPKEY == 2da946d4a48ce9b1045ddd8c
09-06 18:35:36.611 11769-11769/k.agera.com.locationwidget E/---: ---cn.jpush.android.MSG_ID == 361790851
 */
class PushReceiver : BroadcastReceiver() {

    var from = ""
    var listener = object : LocationUtils.onLocateListener {
        override fun onLocate(location: AMapLocation?) {
            location ?: return
            var address = location.address
            var lat = location.latitude
            var long = location.longitude

            PushImp.instance().replyLocationtoAlias(from, LocationData(lat, long, CommonUtils.instance().getNowDate(), address))

        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {

        var bundle = intent?.extras
        if (bundle != null) {

            from = bundle[JPushInterface.EXTRA_TITLE]?.toString() ?: return
            var type = bundle[JPushInterface.EXTRA_CONTENT_TYPE]?.toString() ?: return
            Log.e("---", "---from:$from, type:$type")

            when (type) {
                Config.MESSAGE_LOCATION -> {
                    LocationUtils.instance().startLocation(true, listener)
                }

                Config.MESSAGE_REPLY_LOCATION -> {
                    MyApp.instance().mMapActivity ?: return
                    var location = MyApp.instance().gson.fromJson<LocationData>(bundle.get(JPushInterface.EXTRA_MESSAGE).toString(), LocationData::class.java)
                    //show location on mapview
                    MyApp.instance().mMapActivity?.moveTo(location)
                }
            }


        }
    }


}