package k.agera.com.locationwidget.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.base.BaseActivity
import k.agera.com.locationwidget.utils.CommonUtils
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.utils.ClickObservable

/**
 * Created by Agera on 2018/8/21.
 */
class SignUpActivity : BaseActivity(), Updatable {


    lateinit var mEt_accound: EditText
    lateinit var mEt_password: EditText
    lateinit var mRep: Repository<Result<String>>
    var clickObservable = ClickObservable()

    var activeOnce = Result.failure<Boolean>()

    var account: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_layout)

        mEt_accound = findViewById(R.id.et_account) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText

        initEvents()
    }

    fun initEvents() {

        findViewById(R.id.btn_sign_up).setOnClickListener {
            activeOnce = Result.success(true)
            (MyApp.instance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
            clickObservable.onClick(it)
        }
        mRep = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(clickObservable)
                .onUpdatesPerLoop()
                .attemptGetFrom {
                    activeOnce
                }
                .orSkip()
                .attemptGetFrom {
                    var netInfo = CommonUtils.instance().checkNetworkAvailable()
                    netInfo.ifFailedSendTo {
                        CommonUtils.instance().showShortMessage(mEt_accound, "没有网络..")
                    }
                    netInfo
                }
                .orSkip()
                .attemptGetFrom {
                    account = mEt_accound.text?.toString()
                    password = mEt_password.text?.toString()

                    if (account == null || password == null) {
                        CommonUtils.instance().showLongMessage(mEt_accound, "请输入正确的账号和密码.")
                        Result.failure<String>()
                    }
                    var check = SignImp.instance().checkDataFormat(account!!, password!!)
                    check.ifFailedSendTo {
                        CommonUtils.instance().showLongMessage(mEt_accound, it.message!!)
                    }
                    check
                }
                .orSkip()
                .goTo(TaskDriver.instance().mExecutor)
                .typedResult(String::class.java)
                .thenTransform {
                    CommonUtils.instance().showShortMessage(mEt_accound, "正在注册...")
                    SignImp.instance().signUp(account!!, password!!)
                }
                .notifyIf { _, v2 ->
                    if (v2.failed()) {
                        CommonUtils.instance().showShortMessage(mEt_accound, "注册失败，该账号已经存在...")
                    }
                    v2.succeeded()
                }
                .compile()
        mRep.addUpdatable(this)
    }


    override fun update() {
        CommonUtils.instance().showShortMessage(mEt_accound, "注册成功，跳转登录页面...")
        TaskDriver.instance().mMainHandler.postDelayed({
            startActivity(Intent(SignUpActivity@ this, SignInActivity::class.java))
            finish()
        }, 1_500)
    }


}