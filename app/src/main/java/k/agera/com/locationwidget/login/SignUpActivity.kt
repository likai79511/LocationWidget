package k.agera.com.locationwidget.login

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.RepositoryCompilerStates
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.utils.ClickObservable

/**
 * Created by Agera on 2018/8/21.
 */
class SignUpActivity : Activity(), Updatable {


    lateinit var mEt_accound: EditText
    lateinit var mEt_password: EditText
    lateinit var mRep:Repository<String>
    var clickObservable = ClickObservable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_layout)

        mEt_accound = findViewById(R.id.et_account) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText


        initEvents()

        overridePendingTransition()
    }

    fun initEvents() {

        findViewById(R.id.btn_sign_in).setOnClickListener(clickObservable)

        mRep = Repositories.repositoryWithInitialValue("")
                .observe(clickObservable)
                .onUpdatesPerLoop()
                .getFrom {
                    mEt_accound.text?.toString()
                }

    }


    override fun update() {
    }
}