package k.agera.com.locationwidget.utils

import android.view.View
import com.google.android.agera.BaseObservable

/**
 * Created by Agera on 2018/8/21.
 */
class ClickObservable : View.OnClickListener, BaseObservable() {
    override fun onClick(v: View?) {
        dispatchUpdate()
    }
}