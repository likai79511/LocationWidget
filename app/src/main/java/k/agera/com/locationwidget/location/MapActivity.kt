package k.agera.com.locationwidget.location

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.agera.hometools.bean.LocationData
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.push.PushImp
import k.agera.com.locationwidget.utils.CommonUtils

/**
 * Created by Agera on 2018/9/5.
 */
class MapActivity : Activity() {


    lateinit var mMapView: MapView
    lateinit var mAMap: AMap
    var mUserTel: String? = null
    var isSelf = false
    lateinit var mFab: ImageView
    lateinit var mTvDetail: TextView
    lateinit var mCardDetail: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        MyApp.instance().mMapActivity = this


        mUserTel = intent.getStringExtra("data")?.toString()
        mUserTel?.let {
            isSelf = MyApp.instance().getSelf() == it
        }

        initViews(savedInstanceState)
        initMap()
        initEvents()
        if (!isSelf){
            CommonUtils.instance().showShortMessage(mMapView,"正查定位，请稍等...")
            PushImp.instance().requireLocationByAlias(mUserTel!!)
        }

    }

    private fun initViews(savedInstanceState: Bundle?) {
        mMapView = findViewById(R.id.map) as MapView
        mMapView.onCreate(savedInstanceState)
        mFab = findViewById(R.id.btn_refresh) as ImageView
        mFab.visibility = if (isSelf) View.GONE else View.VISIBLE
        mTvDetail = findViewById(R.id.tv_detail) as TextView
        mCardDetail = findViewById(R.id.cd_detail) as CardView
        mCardDetail.visibility = if (isSelf) View.GONE else View.VISIBLE
    }

    fun initEvents() {
        mFab?.setOnClickListener {
            mUserTel?.let {
                PushImp.instance().requireLocationByAlias(it)
            }
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
        } else {
            mAMap.uiSettings.isZoomControlsEnabled = false
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
        if (location.latitude == 0.0 && location.longitude == 0.0) {
            CommonUtils.instance().showShortMessage(mMapView, "定位失败，请稍后再试...")
            return
        }

        var latlng = LatLng(location.latitude, location.longitude)
        mAMap?.addMarker(MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed)))
        mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(latlng, 14f, 0f, 0f)))
        mTvDetail?.text = "位置: ${location.detail}\nlatitude,longitude: ${location.latitude} , ${location.longitude}\n更新的时间: ${location.time}"

    }
}