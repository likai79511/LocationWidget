package k.agera.com.locationwidget;

import com.google.android.agera.Function;
import com.google.android.agera.Repositories;
import com.google.android.agera.RepositoryCompilerStates;

import k.agera.com.locationwidget.core.TaskDriver;

/**
 * Created by Agera on 2018/8/22.
 */

public class Test {

    {
        RepositoryCompilerStates.REventSource<String, String> stringStringREventSource = Repositories.repositoryWithInitialValue("");
        RepositoryCompilerStates.RFlow<String, String, ?> stringStringRFlow = stringStringREventSource.observe().onUpdatesPerLoop();
        RepositoryCompilerStates.RFlow<String, String, ?> o = stringStringRFlow.goTo(TaskDriver.Companion.instance().getMExecutor());
        o.transform(Function<>);


    }
}
