package k.agera.com.locationwidget.utils

import android.util.Log
import com.google.android.agera.Result
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.bean.ApkInfo
import k.agera.com.locationwidget.network.NetCore
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.RecursiveAction

/**
 * Created by Agera on 2018/9/17.
 */
class AppUpdateUtils private constructor() {

    val apkInfoUrl = "https://raw.githubusercontent.com/likai79511/LocationWidget/master/apk/apkInfo.txt"





    companion object {
        var size: Int = 0
        var mIndex = 0
        lateinit var mDownloadByteArray: ByteArray
        val downloadBlockSize:Int = 4 * 1024 * 1024   //4M
        var downloadUrl = ""

        private var utils = AppUpdateUtils()
        fun instance() = utils
    }


    fun checkUpdate(): Result<String> {
        var result = Result.failure<String>()
        NetCore.instance().doGet(apkInfoUrl, null)
                .ifSucceededSendTo {
                    var appInfo = CommonUtils.instance().gson.fromJson<ApkInfo>(it.bodyString.get(), ApkInfo::class.java)
                    MyApp.instance()?.apply {
                        Log.e("---", "---latest versionCode: ${appInfo.versionCode}, current versionCode:${packageManager.getPackageInfo(packageName, 0).versionCode}")
                        if (appInfo.versionCode > packageManager.getPackageInfo(packageName, 0).versionCode)
                            result = Result.success(appInfo.url)
                    }
                }
        return result
    }

    fun checkApkSize(url: String): Result<Long> {
        var result = Result.failure<Long>()
        NetCore.instance().checkResourceSize(url)
                .ifSucceededSendTo {
                    if (it > 0) {
                        size = it.toInt()
                        result = Result.success(it)
                    }
                }
        return result
    }

    fun downloadApk() {
        mDownloadByteArray = ByteArray(size)
    }

    //Fork Join
    class Task(var index: Int, var blockSize: Int) : RecursiveAction() {
        override fun compute(){
            try {
                if (blockSize <= downloadBlockSize){
                    var conn = URL(downloadUrl).openConnection() as HttpURLConnection
                    conn.requestMethod = "GET"
                    conn.setRequestProperty("Range","bytes=$index-${index+blockSize}")

                    if (conn.responseCode in 200..300){
                        var bytes = conn.inputStream.readBytes()
                        System.arraycopy(bytes,0, mDownloadByteArray,index, bytes.size)
                    }

                }else{

                    var task = Task(mIndex, downloadBlockSize)
                    mIndex+= downloadBlockSize+1
                    var forkTask = Task(mIndex, size-)

                }


            } catch (e: Exception) {

            }
        }

    }
}