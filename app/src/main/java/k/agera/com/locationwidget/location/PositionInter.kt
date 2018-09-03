package k.agera.com.locationwidget.location

import com.google.android.agera.Result

/**
 * Created by Agera on 2018/8/27.
 */
interface PositionInter {

    // get friends list
    fun getFriends():Result<String>


    //check friend had added or if exist in server
    fun checkIfExist(tel: String, nickName: String, friends: ArrayList<String>):Result<String>

}