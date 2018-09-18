package k.agera.com.locationwidget.utils

import android.util.Log
import com.google.android.agera.Result
import k.agera.com.locationwidget.bean.ApkInfo
import k.agera.com.locationwidget.network.NetCore

/**
 * Created by Agera on 2018/9/17.
 */
class AppUpdateUtils private constructor() {

    val apkInfoUrl = "https://raw.githubusercontent.com/likai79511/LocationWidget/master/apk/apkInfo.txt"

    companion object {
        private var utils = AppUpdateUtils()
        fun instance() = utils
    }


    fun checkUpdate(): Result<String> {
        var result = Result.failure<String>()

        NetCore.instance().doGet(apkInfoUrl, null)
                .ifSucceededSendTo {
                    var appInfo = CommonUtils.instance().gson.fromJson<ApkInfo>(it.bodyString.get(), ApkInfo::class.java)

                    Log.e("---", "---result:${appInfo?.toString()}")

                }
        return result
    }


}