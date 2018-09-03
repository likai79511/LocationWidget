package k.agera.com.locationwidget.location

import android.app.AlertDialog
import android.content.Context
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
                            if (friends == null)
                                result = Result.failure<String>(Exception("friends is empty"))
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

    override fun checkIfExist(tel: String, nickName: String, friends: ArrayList<String>): Result<String> {
        var result = Result.failure<String>()
        if (friends.size > 0) {
            friends.forEach {
                if (tel.equals(it.split("-")[0])) {
                    result = Result.failure(Exception("你已经添加过了该好友."))
                    return result
                }
            }
        }

        //check if exist in server db
        NetCore.instance().doGet("${Config.userTable}?where={\"account\":\"$tel\"}", Config.BombHeaders)
                .ifSucceededSendTo {
                    var responseCode = it.responseCode
                    if (responseCode in 200..300) {
                        var data = it.bodyString.get()
                        Log.e("---", "---data:$data")
                        var list = gson.fromJson<BombUserList>(data, BombUserList::class.java)
                        list.results?.let {
                            if (it.size > 0)
                                result = Result.success("success")
                            else
                                result = Result.failure(Exception("该用户不存在."))
                        }
                    }

                }
                .ifFailedSendTo {
                    result = Result.failure(Exception("服务器异常,请稍后重试."))
                }

        return result
    }




}