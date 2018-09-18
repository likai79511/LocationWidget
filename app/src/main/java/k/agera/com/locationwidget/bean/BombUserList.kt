package k.agera.com.locationwidget.bean

import android.support.annotation.Keep

/**
 * Created by Agera on 2018/8/23.
 */
@Keep
data class BombUserList(var results: ArrayList<UserInfo>)

@Keep
data class UserInfo(var account: String,
                    var password: String,
                    var updatedAt: String,
                    var friends:String)