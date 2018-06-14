package k.agera.com.locationwidget.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.EditText
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.map.MapActivity

/**
 * Created by Agera on 2018/6/13.
 */
class LoginActivity : Activity(), LoginView {


    lateinit var mEt_tel: EditText
    lateinit var mEt_password: EditText
    lateinit var mPresenter: LoginPresenter
    var tel: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        mEt_tel = findViewById(R.id.et_tel) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText

        mPresenter = LoginPresenter()
        mPresenter.attachView(this)

        findViewById(R.id.btn_login).setOnClickListener {
            tel = mEt_tel.text?.toString()
            password = mEt_password.text?.toString()
            mPresenter.login(tel,password)
        }
    }

    override fun onSuccess() {
        startActivity(Intent(LoginActivity@ this, MapActivity::class.java))
    }

    override fun onError(msg: String) {
        Snackbar.make(mEt_tel, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}