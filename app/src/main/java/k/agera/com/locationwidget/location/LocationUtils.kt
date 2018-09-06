package k.agera.com.locationwidget.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import k.agera.com.locationwidget.MyApp

/**
 * Created by Agera on 2018/9/5.
 */
class LocationUtils private constructor() {

    companion object {
        private var mUtils = LocationUtils()
        fun instance() = mUtils
    }


    private var mLocationClient: AMapLocationClient = AMapLocationClient(MyApp.instance())


    init {
        mLocationClient.setLocationListener(AMapLocationListener {})
    }

    private lateinit var mlocationOptions: AMapLocationClientOption

    fun startLocation(once: Boolean) {
        mlocationOptions = AMapLocationClientOption()
        mlocationOptions.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy

        mlocationOptions.isOnceLocation = once
        mlocationOptions.isNeedAddress = true
        mlocationOptions.isLocationCacheEnable = false

        mLocationClient.setLocationOption(mlocationOptions)
        mLocationClient.startLocation()
    }

    fun stopListener() {
        mLocationClient?.stopLocation()
    }


}