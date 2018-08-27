package k.agera.com.locationwidget.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.agera.RepositoryCompilerStates
import com.google.android.agera.Result

/**
 * Created by Agera on 2018/8/22.
 */
open class BaseActivity : AppCompatActivity() {

    inline fun <F : RepositoryCompilerStates.RFlow<*, *, *>, T> F.typedResult(value: Class<T>): RepositoryCompilerStates.RFlow<Result<T>, *, *>
            = this as RepositoryCompilerStates.RFlow<Result<T>, *, *>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}