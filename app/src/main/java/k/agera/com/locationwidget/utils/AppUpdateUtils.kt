package k.agera.com.locationwidget.utils

/**
 * Created by Agera on 2018/9/17.
 */
class AppUpdateUtils private constructor() {
    companion object {
        private var utils = AppUpdateUtils()
        fun instance() = utils
    }

}