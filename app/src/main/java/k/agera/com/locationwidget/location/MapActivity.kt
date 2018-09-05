package k.agera.com.locationwidget.location

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.MapView
import k.agera.com.locationwidget.R

/**
 * Created by Agera on 2018/9/5.
 */
class MapActivity : Activity() {

    lateinit var mMapView: MapView
    lateinit var mAMap:AMap

    var mLocationListener = object :LocationUtils.onLocationListener{
        override fun onlocate(latitued: Double, longitude: Double, address: String) {
            Log.e("---", "---onlocate---\naddress:$address\nlatitued:$latitued\nlongitude:$longitude")

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)

        findViewById(R.id.btn_refresh).setOnClickListener {
            LocationUtils.instance().startLocation(true,mLocationListener)
        }


        mMapView = findViewById(R.id.map) as MapView
        mMapView.onCreate(savedInstanceState)

        initMap()
    }

    private fun initMap(){
        mAMap = mMapView.map
        mAMap.isTrafficEnabled = true
        


    }


}