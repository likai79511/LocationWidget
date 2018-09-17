package com.agera.hometools.bean

import android.support.annotation.Keep

/**
 * Created by Agera on 2017/12/15.
 */
@Keep
data class LocationData(var latitude:Double,var longitude:Double,var time:String,var detail:String)