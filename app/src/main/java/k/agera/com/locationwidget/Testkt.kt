package k.agera.com.locationwidget

import android.os.Bundle
import com.google.android.agera.Function
import com.google.android.agera.Repositories
import com.google.android.agera.RepositoryCompilerStates
import k.agera.com.locationwidget.base.BaseActivity
import k.agera.com.locationwidget.core.TaskDriver

/**
 * Created by Agera on 2018/8/29.
 */
class Testkt: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun test(){
        val s1: RepositoryCompilerStates.REventSource<String, String> = Repositories.repositoryWithInitialValue("")
        val s2:RepositoryCompilerStates.RFlow<String, String, *> = s1.observe().onUpdatesPerLoop()
        val s3:RepositoryCompilerStates.RFlow<String, String, *> = s2.goTo(TaskDriver.instance().mExecutor).typedValue(String::class.java)
        val s4:RepositoryCompilerStates.RFlow<String, String, *> = s3.transform(Function<String, String> { "124" })

        val s5:RepositoryCompilerStates.RFlow<String, String, *> = s4.goTo(TaskDriver.instance().mExecutor).typedValue(String::class.java)

    }
}