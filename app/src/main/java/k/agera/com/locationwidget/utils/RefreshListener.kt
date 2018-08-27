package k.agera.com.locationwidget.utils

import android.support.v4.widget.SwipeRefreshLayout
import com.google.android.agera.BaseObservable

/**
 * Created by Agera on 2018/8/27.
 */
class RefreshListener:SwipeRefreshLayout.OnRefreshListener,BaseObservable() {
    override fun onRefresh() {
        dispatchUpdate()
    }

}