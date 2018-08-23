package k.agera.com.locationwidget

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.android.agera.Repositories
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.login.SignImp
import k.agera.com.locationwidget.utils.Constants

/**
 * Created by Agera on 2018/8/23.
 */
class SplashActivity : BaseActivity(), Updatable {

    lateinit var mRoot: View

    var account = ""
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRoot = findViewById(R.id.root)
        initEvents()
    }


    fun initEvents() {
        var mRep = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe()
                .onUpdatesPerLoop()
                .attemptGetFrom {
                    var netInfo = CommonUtils.instance().checkNetworkAvailable()
                    netInfo.ifFailedSendTo {
                        CommonUtils.instance().showShortMessage(mRoot, "没有网络..")
                        gotoSignInPage()
                    }
                    netInfo
                }
                .orSkip()
                .attemptGetFrom {
                    account = CommonUtils.instance().getData(Constants.USERNAME, "").toString()
                    password = CommonUtils.instance().getData(Constants.PASSWORD, "").toString()
                    if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                        gotoSignInPage()
                        return@attemptGetFrom Result.failure<String>()
                    }
                    Result.success("")
                }
                .orSkip()
                .goTo(TaskDriver.instance().mExecutor)
                .typedResult(String::class.java)
                .thenTransform {
                    SignImp.instance().signIn(account, password)
                }
                .notifyIf { _, v2 ->
                    v2.succeeded()
                }
                .compile()
        mRep.addUpdatable(this)

    }

    override fun update() {
        Log.e("---", "---update---")
    }


    private fun gotoSignInPage() {
        Log.e("---", "---gotoSignInPage---")
    }
}