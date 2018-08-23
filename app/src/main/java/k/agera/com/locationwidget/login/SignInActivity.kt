package k.agera.com.locationwidget.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import cn.jpush.android.api.JPushInterface
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.*
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.utils.ClickObservable
import k.agera.com.locationwidget.utils.Constants

/**
 * Created by Administrator on 2018/8/22.
 */
class SignInActivity : BaseActivity(), Updatable {


    lateinit var mEt_accound: EditText
    lateinit var mEt_password: EditText
    lateinit var mRep: Repository<Result<String>>
    var clickObservable = ClickObservable()

    var activeOnce = Result.failure<Boolean>()

    var account: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_layout)

        mEt_accound = findViewById(R.id.et_account) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText

        initEvents()
    }

    private fun initEvents() {
        findViewById(R.id.btn_sign_in).setOnClickListener {
            activeOnce = Result.success(true)
            (MyApp.instance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
            clickObservable.onClick(it)
        }

        findViewById(R.id.btn_sign_up).setOnClickListener {
            (MyApp.instance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
            startActivity(Intent(SignInActivity@ this, SignUpActivity::class.java))
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
                    CommonUtils.instance().showShortMessage(mEt_accound, "正在登陆...")
                    SignImp.instance().signIn(account!!, password!!)
                }
                .notifyIf { _, v2 ->
                    if (v2.failed()) {
                        CommonUtils.instance().showShortMessage(mEt_accound, "登陆失败，账号密码不匹配...")
                    }
                    v2.succeeded()
                }
                .compile()

        mRep.addUpdatable(this)
    }


    override fun update() {
        //save account and password , for auto login when next launch app
        CommonUtils.instance().saveData(Constants.USERNAME, account)
        CommonUtils.instance().saveData(Constants.PASSWORD, password)
        //set alias to jpush
        JPushInterface.setAlias(MyApp.instance(), 0, account)
        //turn to home page
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}