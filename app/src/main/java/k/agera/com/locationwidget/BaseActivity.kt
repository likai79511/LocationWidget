package k.agera.com.locationwidget

import android.app.Activity
import android.os.Bundle
import com.google.android.agera.RepositoryCompilerStates
import com.google.android.agera.Result

/**
 * Created by Agera on 2018/8/22.
 */
open class BaseActivity : Activity() {

    inline fun <F : RepositoryCompilerStates.RFlow<*, *, *>, T> F.typedResult(value: Class<T>): RepositoryCompilerStates.RFlow<Result<T>, *, *>
            = this as RepositoryCompilerStates.RFlow<Result<T>, *, *>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}