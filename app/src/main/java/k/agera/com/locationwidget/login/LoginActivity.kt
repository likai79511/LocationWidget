package k.agera.com.locationwidget.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import k.agera.com.locationwidget.CommonUtils
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.map.MapActivity

/**
 * Created by Agera on 2018/6/13.
 */
class LoginActivity : Activity() {

    lateinit var mEt_tel: EditText
    lateinit var mEt_password: EditText

    var tel: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        mEt_tel = findViewById(R.id.et_tel) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText

        findViewById(R.id.btn_login).setOnClickListener {

            if (!checkDataAvaliable()) return@setOnClickListener

            //login success
            startActivity(Intent(LoginActivity@ this, MapActivity::class.java))
        }
    }

    fun checkDataAvaliable(): Boolean {
        tel = mEt_tel.text?.toString()
        password = mEt_tel.text?.toString()

        if (!CommonUtils.instance().checkTel(tel) || !CommonUtils.instance().checkPassword(password)) {
            CommonUtils.instance().showLongMessage(mEt_tel, "tel or password is incorrect")
            return false
        }


        LoginPresenter.instance().login(tel!!, password!!, object : LoginPresenter.CallBack {
            override fun onSuccess(content: String) {
                Log.e("---", "--result: $content")
            }

            override fun onError() {
                Log.e("---", "--result is error --")
            }

        })


        return true

    }

}