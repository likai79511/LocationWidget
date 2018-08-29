package k.agera.com.locationwidget.location

import android.util.Log
import com.google.android.agera.Result
import com.google.gson.Gson
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.bean.BombUserList
import k.agera.com.locationwidget.network.Config
import k.agera.com.locationwidget.network.NetCore
import k.agera.com.locationwidget.utils.AppendMap

/**
 * Created by Agera on 2018/8/27.
 */
class PositionImp : PositionInter {
    private var gson = Gson()

    companion object {
        private var imp = PositionImp()
        fun instance() = imp
    }


    override fun getFriends(): Result<String> {
        var result = Result.failure<String>()
        NetCore.instance().doGet("${Config.userTable}?where={\"account\":\"${MyApp.instance().selfAlias}\"}", Config.BombHeaders)
                .ifSucceededSendTo {
                    try {
                        var responseCode = it.responseCode
                        if (responseCode in 200..300) {
                            var data = it.bodyString.get()
                            Log.e("---", "---data:$data")
                            var friends = gson.fromJson<BombUserList>(data, BombUserList::class.java).results[0].friends
                            if (friends == null || friends.isEmpty())
                                result = Result.failure<String>()
                            else
                                result = Result.success(friends)
                        }
                    } catch (e: Exception) {
                        Log.e("---", "--appear error:${e.message}")
                        result = Result.failure()
                    }
                }
                .ifFailedSendTo {
                    Log.e("---", "---signUp appear error: ${it.message}")
                    result = Result.failure()
                }
        return result
    }


    fun parseFriends(friends: String): HashMap<String, String> {
        var map = AppendMap<String, String>()
        try {
            var friendsArray = friends.split(",")
            friendsArray.forEach {
                map.put(it.split("-")[0], it.split("-")[1])
            }
        } catch (e: Exception) {
            Log.e("---", "---parseFriends appear error:${e.message}")
        }
        return map.compile()
    }

}