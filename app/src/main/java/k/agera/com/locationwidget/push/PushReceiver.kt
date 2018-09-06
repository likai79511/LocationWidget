package k.agera.com.locationwidget.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

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
    override fun onReceive(context: Context?, intent: Intent?) {

        var bundle = intent?.extras
        if (bundle != null) {

            bundle.keySet().forEach {
                Log.e("---", "---$it == ${bundle.get(it)}");
            }

        }
    }
}