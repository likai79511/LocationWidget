package k.agera.com.locationwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.push.PushImp
import k.agera.com.locationwidget.utils.Constants

/**
 * Created by Agera on 2018/6/14.
 */
class CommonUtils private constructor() {

    var ctx = MyApp.instance()

    companion object {
        private var instance: CommonUtils = CommonUtils()
        fun instance() = instance
    }

    fun showShortMessage(payload: View, message: String) {
        TaskDriver.instance().mMainHandler.post {
            if (payload == null) {
                Toast.makeText(MyApp.instance(), message, Toast.LENGTH_SHORT).show()
            } else {
                Snackbar.make(payload, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun showLongMessage(payload: View?, message: String) {
        TaskDriver.instance().mMainHandler.post {
            if (payload == null) {
                Toast.makeText(MyApp.instance(), message, Toast.LENGTH_LONG).show()
            } else {
                Snackbar.make(payload, message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Save data to SD card
     */
    fun <T> saveData(key: String, value: T): Boolean {
        val edit = MyApp.instance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE).edit()
        if (value is String) {
            return edit.putString(key, value).commit()
        } else if (value is Boolean) {
            return edit.putBoolean(key, value).commit()
        } else if (value is Int) {
            return edit.putInt(key, value).commit()
        } else if (value is Long) {
            return edit.putLong(key, value).commit()
        } else if (value is Float) {
            return edit.putFloat(key, value).commit()
        }
        return false
    }

    /**
     * Get Data from SD card
     * @param key
     * *
     * @param dValue
     * *
     * @return
     */
    fun getData(key: String, dValue: Any): Any? {
        val sp = MyApp.instance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE)
        if (dValue is String) {
            return sp.getString(key, dValue)
        } else if (dValue is Boolean) {
            return sp.getBoolean(key, dValue)
        } else if (dValue is Int) {
            return sp.getInt(key, dValue)
        } else if (dValue is Float) {
            return sp.getFloat(key, dValue)
        } else if (dValue is Long) {
            return sp.getLong(key, dValue)
        }
        return null
    }

    fun clearData(vararg keys: String): Boolean? {
        try {
            for (key in keys) {
                MyApp.instance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE).edit().remove(key).commit()
            }
        } catch (e: Exception) {
            Log.e("---", "---clearData encounter error:" + e.message)
            return false
        }
        return true
    }


    fun saveAccountInfo(account: String, password: String) = saveData(Constants.USERNAME, account) && saveData(Constants.PASSWORD, password)


    fun checkTel(tel: String?): Boolean = !TextUtils.isEmpty(tel) && tel!!.length == 11

    fun checkPassword(password: String?): Boolean = !TextUtils.isEmpty(password) && password!!.length >= 6

    fun checkConfirmPassword(pass1: String, pass2: String): Boolean = !TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2) && pass1.equals(pass2)

    fun startDaemon() {
        var am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = PendingIntent.getService(ctx, 0, Intent(ctx, DaemonService::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60_000, intent)
    }


    class DaemonService : Service() {
        override fun onBind(intent: Intent?): IBinder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            PushImp.instance().resumeService()
            return super.onStartCommand(intent, flags, startId)
        }
    }


    class BootReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            CommonUtils.instance.startDaemon()
        }
    }
}