package k.agera.com.locationwidget.bean

import android.support.annotation.Keep

/**
 * Created by Agera on 2018/9/4.
 */
@Keep
data class BombTableRowUpdate(var affectedRows: Int,
                              var updatedAt: String)