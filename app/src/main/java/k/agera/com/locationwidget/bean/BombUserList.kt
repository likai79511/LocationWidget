package k.agera.com.locationwidget.bean

/**
 * Created by Agera on 2018/8/23.
 */
data class BombUserList(var results: ArrayList<UserInfo>)


data class UserInfo(var account: String,
                    var password: String,
                    var updatedAt: String,
                    var friends:String)