package k.agera.com.locationwidget.location

import com.google.android.agera.Result
import k.agera.com.locationwidget.bean.Friend

/**
 * Created by Agera on 2018/8/27.
 */
interface PositionInter {

    fun getFriends():Result<ArrayList<Friend>>


}