package k.agera.com.locationwidget.location

import android.util.Log
import com.google.android.agera.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.bean.BombUserList
import k.agera.com.locationwidget.bean.Friend
import k.agera.com.locationwidget.network.Config
import k.agera.com.locationwidget.network.NetCore
import k.agera.com.locationwidget.utils.AppendList

/**
 * Created by Agera on 2018/8/27.
 */
class PositionImp : PositionInter {
    private var gson = Gson()

    companion object {
        private var imp = PositionImp()
        fun instance() = imp
    }


    override fun getFriends(): Result<ArrayList<Friend>> {
        var result = Result.failure<ArrayList<Friend>>()
        NetCore.instance().doGet("${Config.userTable}?where={\"account\":\"${MyApp.instance().selfAlias}\"}", Config.BombHeaders)
                .ifSucceededSendTo {
                    try {
                        var responseCode = it.responseCode
                        if (responseCode in 200..300) {
                            var data = it.bodyString.get()
                            Log.e("---", "---data:$data")
                            var list = gson.fromJson<BombUserList>(data, BombUserList::class.java)

                            list.results?.let {
                                if (it.size == 0) {
                                    result = Result.failure()
                                } else {
                                    var friends = it[0].friends
                                    if (null == friends || friends.isEmpty()) {
                                        result = Result.success(AppendList<Friend>().compile())
                                    } else {
                                        result = Result.success(gson.fromJson(friends, object : TypeToken<ArrayList<Friend>>() {
                                        }.type))
                                    }

                                }
                            }
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


}