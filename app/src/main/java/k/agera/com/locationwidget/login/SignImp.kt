package k.agera.com.locationwidget.login

import android.util.Log
import com.google.android.agera.Result
import com.google.gson.Gson
import k.agera.com.locationwidget.bean.User
import k.agera.com.locationwidget.network.Config
import k.agera.com.locationwidget.network.NetCore

/**
 * Created by Agera on 2018/8/21.
 */
class SignImp : SignInter {

    var gson = Gson()
    lateinit var result: Result<String>

    companion object {
        var imp = SignImp()
        fun instance() = imp
    }


    override fun checkDataFormat(account: String, password: String): Result<Pair<String, String>> {
        if (account.length != 11)
            return Result.failure(Exception("无效的手机号"))
        if (password.length < 6)
            return Result.failure(Exception("密码长度必须大于6位"))
        return Result.success(Pair(account, password))
    }

    override fun signUp(account: String, password: String): Result<String> {
        NetCore.instance().doPost(Config.userTable, Config.BombHeaders, gson.toJson(User(account, password)))
                .ifSucceededSendTo {
                    var responseCode = it.responseCode
                    if (responseCode !in 200..300) {
                        result = Result.failure()
                        return@ifSucceededSendTo
                    }
                    var data = it.bodyString.get()
                    result = Result.success(data)
                }
                .ifFailedSendTo {
                    Log.e("---", "---signUp appear error: ${it.message}")
                    result = Result.failure()
                }
        return result
    }

    override fun signIn(account: String, password: String): Result<String> {
        NetCore.instance().doGet("${Config.userTable}?where{\"account\":\"$account\",\"password\":\"$password\"}", Config.BombHeaders)
                .ifSucceededSendTo {
                    var responseCode = it.responseCode
                    if (responseCode !in 200..300) {
                        result = Result.failure()
                        return@ifSucceededSendTo
                    }
                    var data = it.bodyString.get()
                    Log.e("---","---data:$data")
                    result = Result.failure()
//                    result = Result.success(data)
                }
                .ifFailedSendTo {
                    Log.e("---", "---signUp appear error: ${it.message}")
                    result = Result.failure()
                }
        return result
    }
}