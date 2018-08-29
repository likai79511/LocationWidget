package k.agera.com.locationwidget;

import android.support.annotation.NonNull;

import com.google.android.agera.Function;
import com.google.android.agera.Repositories;
import com.google.android.agera.RepositoryCompilerStates;

import k.agera.com.locationwidget.core.TaskDriver;

/**
 * Created by Agera on 2018/8/22.
 */

public class Test {

    {

        RepositoryCompilerStates.REventSource<String, String> s1 = Repositories.repositoryWithInitialValue("");
        RepositoryCompilerStates.RFlow<String, String, ?> s2 = s1.observe().onUpdatesPerLoop();
        RepositoryCompilerStates.RFlow<String, String, ?> s3 = s2.goTo(TaskDriver.Companion.instance().getMExecutor());
        RepositoryCompilerStates.RFlow<String, String, ?> s4 = s3.transform(new Function<String, String>() {
            @NonNull
            @Override
            public String apply(@NonNull String input) {
                return "124";
            }
        });

        RepositoryCompilerStates.RFlow<String, String, ?> s5 = s4.goTo(TaskDriver.Companion.instance().getMExecutor());


    }
}
