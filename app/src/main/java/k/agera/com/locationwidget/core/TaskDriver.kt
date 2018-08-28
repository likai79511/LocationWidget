package k.agera.com.locationwidget.core

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

/**
 * Created by Agera on 2018/6/14.
 */
class TaskDriver {

    companion object {
        private val taskDriver = TaskDriver()
        fun instance() = taskDriver
    }

    private val cpuCount = Runtime.getRuntime().availableProcessors()

    private val maxParallelCount = if (cpuCount > 4) cpuCount else 4

    val mControl = Semaphore(maxParallelCount)

    val mMainHandler = Handler(Looper.getMainLooper())

    val mExecutor = Executors.newFixedThreadPool(maxParallelCount)

    val mMainExecutor = Executor{
        mMainHandler.post(it)
    }


    fun execute(task: Runnable) {
        try {
            mControl.acquire()
            mExecutor.submit(task)
        } catch (e: Exception) {
            Log.e("---", "--appear error: ${e.localizedMessage}")
        } finally {
            mControl.release()
        }
    }

    fun executeOnMainThread(task: Runnable) {
        mMainHandler.post(task)
    }

}