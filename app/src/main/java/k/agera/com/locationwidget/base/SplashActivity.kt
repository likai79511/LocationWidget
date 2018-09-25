package k.agera.com.locationwidget.base

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.login.SignImp
import k.agera.com.locationwidget.login.SignInActivity
import k.agera.com.locationwidget.push.PushImp
import k.agera.com.locationwidget.utils.AppUpdateUtils
import k.agera.com.locationwidget.utils.CommonUtils
import k.agera.com.locationwidget.utils.Constants

/**
 * Created by Agera on 2018/8/23.
 */
class SplashActivity : BaseActivity(), Updatable {

    private lateinit var mRoot: View
    private lateinit var mImg: ImageView

    var account = ""
    var password = ""
    var startTime = 0L

    lateinit var mRep: Repository<Result<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
        mRoot = findViewById(R.id.root)!!
        mImg = findViewById(R.id.img) as ImageView

        startTime = System.currentTimeMillis()

        var d: AnimatedVectorDrawable = getDrawable(R.drawable.logo_anim_vector) as AnimatedVectorDrawable
        d?.let {
            mImg.setImageDrawable(it)
            it.start()
        }

        initEvents()
    }


    fun initEvents() {

        mRep = Repositories.repositoryWithInitialValue(Result.absent<String>())
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
                    if (v2.failed()) {
                        //clear account
                        CommonUtils.instance().clearData(Constants.USERNAME)
                        CommonUtils.instance().clearData(Constants.PASSWORD)
                        gotoSignInPage()
                    }
                    v2.succeeded()
                }
                .compile()

        //app update checking
        var mUpdate = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe()
                .onUpdatesPerLoop()
                .goTo(TaskDriver.instance().mExecutor)
                .typedResult(String::class.java)
                .attemptGetFrom {
                    AppUpdateUtils.instance().checkUpdate() }
                .orEnd { notifyDownStream() }
                .attemptTransform {
                    AppUpdateUtils.instance().checkApkSize(it)
                }
                .orEnd { notifyDownStream() }
                .thenTransform {
                    var sizeStr = CommonUtils.instance().formatSize(it)
                    if (!TextUtils.isEmpty(sizeStr))
                        Result.success(sizeStr)
                    else
                        Result.failure()
                }
                .notifyIf { _, v2 ->
                    v2.succeeded()
                }
                .compile()
        mUpdate.addUpdatable({
            var sizeStr = mUpdate.get().get()
            Log.e("---","---app checking update: $sizeStr")
        })

    }

    override fun update() {
        Log.e("---","--truely update--")
        PushImp.instance().setPushAccount(account)

        var duration = System.currentTimeMillis() - startTime
        mRoot.postDelayed({
            startActivity(Intent(SplashActivity@ this, MainActivity::class.java))
            finish()
        }, if (duration > 2_500) 0 else 2_500 - duration)
    }


    private fun gotoSignInPage() {
        var duration = System.currentTimeMillis() - startTime
        mRoot.postDelayed({
            startActivity(Intent(SplashActivity@ this, SignInActivity::class.java))
            finish()
        }, if (duration > 2_500) 0 else 2_500 - duration)
    }


    private fun notifyDownStream(): Result<String> {
        TaskDriver.instance().executeOnMainThread(Runnable {
            Log.e("---","---notifyDownStream---")
            mRep.addUpdatable(this)
        })
        return Result.failure()
    }
}