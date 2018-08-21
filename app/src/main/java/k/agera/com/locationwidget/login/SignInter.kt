package k.agera.com.locationwidget.login

import com.google.android.agera.Result

/**
 * Created by Agera on 2018/8/21.
 */
interface SignInter {

    fun checkDataFormat(account:String,password:String): Result<Pair<String, String>>

    fun signUp(account:String,password:String):Result<String>

    fun signIn(account:String,password:String):Result<String>

}