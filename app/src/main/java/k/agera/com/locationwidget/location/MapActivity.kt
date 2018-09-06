package k.agera.com.locationwidget.location

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.TextView
import com.agera.hometools.bean.LocationData
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.R

/**
 * Created by Agera on 2018/9/5.
 */
class MapActivity : Activity() {


    lateinit var mMapView: MapView
    lateinit var mAMap: AMap
    var mUserTel: String? = null
    var isSelf = false
    lateinit var mFab: FloatingActionButton
    lateinit var mTvDetail: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        MyApp.instance().mMapActivity = this


        /*  mUserTel = intent.getStringExtra("data")
          mUserTel?.let {
              isSelf = MyApp.instance().selfAlias == it
          }*/
        isSelf = true

        initViews(savedInstanceState)
        initMap()
        initEvents()
    }

    private fun initViews(savedInstanceState: Bundle?) {
        mMapView = findViewById(R.id.map) as MapView
        mMapView.onCreate(savedInstanceState)
        mFab = findViewById(R.id.btn_refresh) as FloatingActionButton
        mFab.visibility = if (isSelf) View.GONE else View.VISIBLE
        mTvDetail = findViewById(R.id.tv_detail) as TextView
        mTvDetail.visibility = if (isSelf) View.GONE else View.VISIBLE
    }

    fun initEvents() {
        mFab?.setOnClickListener {

        }
    }


    private fun initMap() {
        mAMap = mMapView.map
        mAMap.isTrafficEnabled = true
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
        var locationStyle = MyLocationStyle()

        if (isSelf) {
            mAMap.myLocationStyle = locationStyle
            mAMap.isMyLocationEnabled = true
            LocationUtils.instance().startLocation(!isSelf)
        }else{
            //request location data

        }
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()

    }

    override fun onPause() {
        super.onPause()
        LocationUtils.instance().stopListener()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationUtils.instance().stopListener()
        mMapView.onDestroy()
        MyApp.instance().mMapActivity = null
    }


    fun moveTo(location: LocationData) {

    }
}