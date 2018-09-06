package k.agera.com.locationwidget.push

import k.agera.com.locationwidget.bean.PushMessage


/**
 * Created by Agera on 2018/6/13.
 */
interface PushInter {
    //set push Alias
    fun setPushAccount(alias: String)

    //require location by alisa
    fun requireLocationByAlias(alisa: String)

    //check and resume
    fun resumeService()

    fun makeMessageModule(): PushMessage

}