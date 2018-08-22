package k.agera.com.locationwidget;

import android.support.annotation.NonNull;

import com.google.android.agera.Repositories;
import com.google.android.agera.RepositoryCompilerStates;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import k.agera.com.locationwidget.core.TaskDriver;

/**
 * Created by Agera on 2018/8/22.
 */

public class Test {

    {
        RepositoryCompilerStates.REventSource<String, String> stringStringREventSource = Repositories.repositoryWithInitialValue("");
        RepositoryCompilerStates.RFlow<String, String, ?> stringStringRFlow = stringStringREventSource.observe().onUpdatesPerLoop();
        RepositoryCompilerStates.RFlow<String, String, ?> o = stringStringRFlow.goTo(TaskDriver.Companion.instance().getMExecutor());
        RepositoryCompilerStates.RTermination<String, Throwable, RepositoryCompilerStates.RFlow<String, String, ?>> stringThrowableRFlowRTermination = o.attemptGetFrom(new Supplier<Result<String>>() {

            @NonNull
            @Override
            public Result<String> get() {
                return Result.success("");
            }
        });


    }
}
