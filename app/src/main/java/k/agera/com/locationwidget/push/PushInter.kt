package k.agera.com.locationwidget.push

import android.content.Context
import com.agera.hometools.bean.LocationData

/**
 * Created by Agera on 2018/6/13.
 */
interface PushInter {
    //set push Alias
    fun setPushAccount(ctx: Context, alias: String)

    //require location by alisa
    fun requireLocationByAlias(alisa:String)

    //require location bt tag
    fun requireLocationByTag(tag:String)

    //send message
    fun sendLocationTo(location: LocationData, to:String)

    //check and resume
    fun resumeService()
}