package k.agera.com.locationwidget.location

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.adapter.PositionAdapter
import k.agera.com.locationwidget.base.BaseActivity
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.observable.RefreshObservable
import k.agera.com.locationwidget.utils.AppendMap

/**
 * Created by Agera on 2018/8/27.
 */
class PositionActivity : BaseActivity(), Updatable {


    private lateinit var mRv: RecyclerView
    private lateinit var mSwipe: SwipeRefreshLayout
    private var mAdapter = PositionAdapter()

    var startShowRefreshIndicator = 0L

    private var mRefreshListener = object : RefreshObservable() {
        override fun onRefresh() {
            super.onRefresh()
            startShowRefreshIndicator = System.currentTimeMillis()
        }
    }

    private var mFriends = AppendMap<String, String>()


    private lateinit var mRefresh_repo: Repository<Result<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.position_layout)

        mRv = findViewById(R.id.lv) as RecyclerView
        mSwipe = findViewById(R.id.swipe) as SwipeRefreshLayout
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
        mRv.layoutManager = LinearLayoutManager(this)
        mRv.adapter = mAdapter
        mSwipe.setOnRefreshListener(mRefreshListener)

        startToRefresh()
        initDialog()
        initEvents()

    }


    private fun initEvents() {
        mRefresh_repo = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(mRefreshListener)
                .onUpdatesPerLoop()
                .goTo(TaskDriver.instance().mExecutor)
                .typedResult(String::class.java)
                .thenTransform {
                    Log.e("---", "--step-01--")
                    PositionImp.instance().getFriends()
                }
                .notifyIf { _, v2 ->
                    if (v2.failed()) {
                        closeRefresh()
                    }
                    v2.succeeded()
                }
                .compile()
        mRefresh_repo.addUpdatable(this)
    }


    //do remove/add action when server response is ok
    override fun update() {

        closeRefresh()
        var friends = mRefresh_repo.get().get()
        mAdapter.setFriendList(friends)
        Log.e("---", "---friends:" + friends)

    }

    private fun closeRefresh() {
        var duration = System.currentTimeMillis() - startShowRefreshIndicator
        mSwipe.postDelayed({
            mSwipe.isRefreshing = false
        }, if (duration >= 2_500) 0 else 2_500 - duration)
    }

    private fun startToRefresh() {
        startShowRefreshIndicator = System.currentTimeMillis()
        mSwipe.post {
            mSwipe.isRefreshing = true
        }
    }


    fun initDialog(){

    }
}