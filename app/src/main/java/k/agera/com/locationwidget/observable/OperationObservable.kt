package k.agera.com.locationwidget.observable

import com.google.android.agera.BaseObservable

/**
 * Created by Agera on 2018/9/3.
 */
class OperationObservable : BaseObservable() {
    fun onClick() {
        dispatchUpdate()
    }
}