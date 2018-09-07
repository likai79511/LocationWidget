package k.agera.com.locationwidget.push

import com.agera.hometools.bean.LocationData
import k.agera.com.locationwidget.bean.PushMessage


/**
 * Created by Agera on 2018/6/13.
 */
interface PushInter {
    //set push Alias
    fun setPushAccount(alias: String)

    //require location by alisa
    fun requireLocationByAlias(alisa: String)

    fun replyLocationtoAlias(alias: String,location:LocationData)

    //check and resume
    fun resumeService()

    fun makeMessageModule(to:String,type:String,msg:String): PushMessage


}