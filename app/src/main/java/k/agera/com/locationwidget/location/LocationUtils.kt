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

    lateinit var mListener: onLocationListener

    private lateinit var mLocationClient: AMapLocationClient
    private var mLocationListener = AMapLocationListener { location ->

        var address = location.address
        var latitued = location.latitude
        var longitude = location.longitude
        mListener?.let {
            it.onlocate(latitued, longitude, address)
        }
    }


    init {
        mLocationClient = AMapLocationClient(MyApp.instance())
        mLocationClient.setLocationListener(mLocationListener)
    }

    private lateinit var mlocationOptions: AMapLocationClientOption

    fun startLocation(once: Boolean, listener: onLocationListener) {
        mlocationOptions = AMapLocationClientOption()
        mlocationOptions.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy

        mlocationOptions.isOnceLocation = once
        mlocationOptions.isNeedAddress = true
        mlocationOptions.isLocationCacheEnable = false

        mLocationClient.setLocationOption(mlocationOptions)
        mLocationClient.startLocation()
    }

    interface onLocationListener {
        fun onlocate(latitued: Double, longitude: Double, address: String)
    }
}