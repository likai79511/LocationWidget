package k.agera.com.locationwidget.location

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.google.android.agera.Repositories
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.adapter.PositionAdapter
import k.agera.com.locationwidget.base.BaseActivity
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.utils.RefreshListener

/**
 * Created by Agera on 2018/8/27.
 */
class PositionActivity : BaseActivity(), Updatable {


    private lateinit var mRv: RecyclerView
    private lateinit var mSwipe: SwipeRefreshLayout
    private var mAdapter = PositionAdapter()

    private var mRefreshListener = RefreshListener()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.position_layout)

        mRv = findViewById(R.id.lv) as RecyclerView
        mSwipe = findViewById(R.id.swipe) as SwipeRefreshLayout
        mRv.adapter = mAdapter
        mSwipe.setOnRefreshListener(mRefreshListener)

        mSwipe.post {
            mSwipe.isRefreshing = true
        }

        initEvents()


    }


    fun initEvents() {
        Repositories.repositoryWithInitialValue("")
                .observe(mRefreshListener)
                .onUpdatesPerLoop()
                .goTo(TaskDriver.instance().mExecutor)
                .typedResult(String::class.java)
                .transform {
                    PositionImp.instance().getFriends()
                }
                .goTo(TaskDriver.instance().mMainExecutor)
                .attemptTransform {
                    

                }

    }


    //do remove/add action when server response is ok
    override fun update() {
        mSwipe.isRefreshing = false
    }

    fun closeRefresh() {
        mSwipe.post {
            mSwipe.isRefreshing = false
        }
    }
}