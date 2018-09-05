package k.agera.com.locationwidget.location

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import k.agera.com.locationwidget.R

/**
 * Created by Agera on 2018/9/5.
 */
class MapActivity : Activity(), LocationSource {


    lateinit var mMapView: MapView
    lateinit var mAMap: AMap
    var mBluePoint: LocationSource.OnLocationChangedListener? = null

    var mLocationListener = object : LocationUtils.onLocationListener {
        override fun onlocate(location: AMapLocation) {
            var address = location.address
            var latitued = location.latitude
            var longitude = location.longitude
            Log.e("---", "---onlocate---\naddress:$address\nlatitued:$latitued\nlongitude:$longitude")
            mBluePoint?.let {
                it.onLocationChanged(location)
                Log.e("---","---refresh blue point---")
            }
            mAMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitued,longitude), 18f));
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)

        findViewById(R.id.btn_refresh).setOnClickListener {
            LocationUtils.instance().startLocation(true, mLocationListener)
        }


        mMapView = findViewById(R.id.map) as MapView
        mMapView.onCreate(savedInstanceState)

        initMap()
    }

    private fun initMap() {
        mAMap = mMapView.map
        mAMap.isTrafficEnabled = true


        mMapView.z
        var locationStyle = MyLocationStyle()
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        mAMap.setLocationSource(this)

    }

    override fun deactivate() {
        mBluePoint = null
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        mBluePoint = p0
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }


}