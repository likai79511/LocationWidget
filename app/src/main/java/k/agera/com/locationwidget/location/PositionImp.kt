package k.agera.com.locationwidget.location

import android.util.Log
import com.google.android.agera.Result
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.bean.BombTableRowUpdate
import k.agera.com.locationwidget.bean.BombUserList
import k.agera.com.locationwidget.network.Config
import k.agera.com.locationwidget.network.NetCore
import k.agera.com.locationwidget.utils.AppendMap

/**
 * Created by Agera on 2018/8/27.
 */
class PositionImp : PositionInter {


    companion object {
        private var imp = PositionImp()
        fun instance() = imp
    }


    override fun getFriends(): Result<String> {
        var result = Result.failure<String>()
        NetCore.instance().doGet("${Config.userTable}?where={\"account\":\"${MyApp.instance().getSelf()}\"}", Config.BombHeaders)
                .ifSucceededSendTo {
                    try {
                        var responseCode = it.responseCode
                        if (responseCode in 200..300) {
                            var data = it.bodyString.get()
                            var friends = MyApp.instance().gson.fromJson<BombUserList>(data, BombUserList::class.java).results[0].friends
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

    override fun checkIfExist(tel: String, friends: ArrayList<String>?): Result<String> {
        var result = Result.failure<String>()
        if (friends != null && friends.size > 0) {
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
                        var list = MyApp.instance().gson.fromJson<BombUserList>(data, BombUserList::class.java)
                        list.results?.let {
                            if (it.size > 0)
                                result = Result.success("该用户存在.")
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

    override fun UpdateFriends(friends: String): Result<String> {
        var result = Result.failure<String>()
        NetCore.instance().doPut("${Config.userTable}?where={\"account\":\"${MyApp.instance().getSelf()}\"}", Config.BombHeaders,"{\"${Config.TableRow_Friends}\":\"$friends\"}")
                .ifSucceededSendTo {
                    try {
                        var responseCode = it.responseCode
                        if (responseCode in 200..300) {
                            var data = it.bodyString.get()
                            var update = MyApp.instance().gson.fromJson<BombTableRowUpdate>(data, BombTableRowUpdate::class.java)
                            Log.e("---","--update::$update")
                            if (update == null || update.affectedRows<1)
                                result = Result.failure<String>(Exception("update failed"))
                            else
                                result = Result.success("update success")
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