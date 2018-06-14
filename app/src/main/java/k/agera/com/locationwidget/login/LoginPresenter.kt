package k.agera.com.locationwidget.login

/**
 * Created by Agera on 2018/6/14.
 */
class LoginPresenter() : LoginCallback {


    private var mViewHelper: LoginView? = null
    private var model = LoginModel.instance(this)

    fun attachView(vh: LoginView) {
        mViewHelper = vh
    }

    fun detachView() {
        mViewHelper = null
    }


    fun isViewAttached() = mViewHelper != null


    fun checkDataFormat(tel: String?, password: String?): Boolean {
        if (!isViewAttached())
            return false

        if (!model.checkTel(tel)) {
            mViewHelper?.onError("tel is incorrect")
            return false
        }

        if (!model.checkPassword(password)) {
            mViewHelper?.onError("password is incorrect")
            return false
        }
        return true
    }

    fun login(tel: String?, password: String?) {
        if (!checkDataFormat(tel, password)) return
        model.login(tel!!,password!!)
    }

    override fun onsuccess() {

    }

    override fun onError() {
    }
}