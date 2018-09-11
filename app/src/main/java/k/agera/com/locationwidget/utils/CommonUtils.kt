package k.agera.com.locationwidget.utils

import android.app.AlertDialog
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.support.design.widget.Snackbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.agera.Result
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.push.PushImp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Agera on 2018/6/14.
 */
class CommonUtils private constructor() {

    var ctx = MyApp.instance()
    var dateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")

    companion object {
        private var instance: CommonUtils = CommonUtils()
        fun instance() = instance
    }

    fun showShortMessage(payload: View?, message: String) {
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


    fun getNowDate() = dateFormat.format(Date())

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

    fun getScreenSize(ctx: Context): Pair<Int, Int> {
        var wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display = wm.defaultDisplay
        var point = Point()
        display.getSize(point)
        return Pair<Int, Int>(point.x, point.y)
    }

    fun checkNetworkAvailable(): Result<String> {
        var cm: ConnectivityManager = MyApp.instance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return Result.failure()
        var netInfo = cm.activeNetworkInfo
        if (netInfo == null || !netInfo.isAvailable)
            return Result.failure()
        return Result.success("")
    }


    fun checkTelephone(tel: String?): Boolean {
        if (tel == null)
            return false
        return tel.trim().length == 11
    }

    fun checkNickName(nickName: String?): Boolean {
        if (nickName == null || nickName.trim().isEmpty())
            return false
        return true
    }


    fun dp2px(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MyApp.instance().resources.displayMetrics).toInt()


    fun makeDialog(ctx: Context, content: View): AlertDialog.Builder {
        var ad = AlertDialog.Builder(ctx)
        var screenSize = getScreenSize(ctx)
        ad.setCancelable(false)
        content.layoutParams = LinearLayout.LayoutParams((screenSize.first * 0.8).toInt(), (screenSize.second * 0.4).toInt())
        ad.setView(content)
        return ad
    }

    fun startDaemon() {
        var build = JobInfo.Builder(0, ComponentName(ctx.baseContext, DaemonService::class.java))
        build.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        build.setPeriodic(if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) 15 * 60 * 1000 else 10 * 60 * 1000)
        build.setPersisted(true)
        (ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(build.build())
    }


    class DaemonService : JobService() {
        override fun onStopJob(params: JobParameters?): Boolean {
            return true
        }

        override fun onStartJob(params: JobParameters?): Boolean {
            PushImp.instance().resumeService()
            return true
        }

    }
}