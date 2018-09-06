package k.agera.com.locationwidget.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by Agera on 2018/9/6.
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