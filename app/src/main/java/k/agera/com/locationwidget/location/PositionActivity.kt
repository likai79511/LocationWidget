package k.agera.com.locationwidget.location

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.adapter.PositionAdapter
import k.agera.com.locationwidget.base.BaseActivity
import k.agera.com.locationwidget.core.TaskDriver
import k.agera.com.locationwidget.observable.OperationObservable
import k.agera.com.locationwidget.observable.RefreshObservable
import k.agera.com.locationwidget.utils.CommonUtils

/**
 * Created by Agera on 2018/8/27.
 */
class PositionActivity : BaseActivity(), Updatable {

    enum class Operation {
        REFRESH,
        ADD,
        REMOVE
    }

    private var mState = Operation.REFRESH

    private lateinit var mRv: RecyclerView
    private lateinit var mSwipe: SwipeRefreshLayout
    private var mAdapter = PositionAdapter()

    var startShowRefreshIndicator = 0L

    private var mRefreshListener = object : RefreshObservable() {
        override fun onRefresh() {
            super.onRefresh()
            startShowRefreshIndicator = System.currentTimeMillis()
            mState = Operation.REFRESH
        }
    }

    private var mFriends = ArrayList<String>()


    private lateinit var mRefresh_repo: Repository<Result<String>>

    private lateinit var mFriendManager: Repository<Result<String>>

    private lateinit var mDg_AddFriend: AlertDialog.Builder

    private var OperationObserveable = OperationObservable()

    private var friend_tel: String? = null
    private var friend_nickname: String? = null

    private var skipFirstIn = Result.failure<String>()


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
        initEvents()
    }


    private fun initEvents() {
        mFriendManager = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(OperationObserveable)
                .onUpdatesPerLoop()
                .attemptGetFrom { skipFirstIn }
                .orSkip()
                .goTo(TaskDriver.instance().mMainExecutor)
                .typedResult(String::class.java)
                .attemptGetFrom {
                    var result = CommonUtils.instance().checkNetworkAvailable()
                    CommonUtils.instance().showShortMessage(mRv, "没有网络连接...")
                    result
                }
                .orSkip()
                .attemptGetFrom {
                    var result = PositionImp.instance().checkIfExist(friend_tel!!, mFriends)
                    if (result.failed())
                        CommonUtils.instance().showShortMessage(mRv, result.failure.message!!)
                    result
                }
                .orSkip()
                .attemptGetFrom {
                    PositionImp.instance().getFriends()
                }
                .orSkip()
                .transform {
                    var friends = if (it == null || it.isEmpty()) {
                        "$friend_tel-$friend_nickname"
                    } else {
                        "$it,$friend_tel-$friend_nickname"
                    }
                    friends
                }
                .thenTransform {
                    PositionImp.instance().setFriendsInServer(it)
                }
                .notifyIf { _, v2 ->
                    if (v2.failed()) {

                    }
                    v2.succeeded()
                }
                .compile()

        mRefresh_repo = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(mRefreshListener, mFriendManager)
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

        findViewById(R.id.btn_add).setOnClickListener {
            showAddFriendDialog()
        }
    }


    //do remove/add action when server response is ok
    override fun update() {
        if (mState == Operation.REFRESH) {
            closeRefresh()
            var friends = mRefresh_repo.get().get()
            mAdapter.setFriendList(friends)
            mFriends = mAdapter.getFriendList()
            Log.e("---", "---friends:" + friends)
        }
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

    private fun showAddFriendDialog() {
        var v = View.inflate(this, R.layout.add_friend_dialog, null)
        mDg_AddFriend = CommonUtils.instance().makeDialog(this, v)
        mDg_AddFriend.setPositiveButton(R.string.ok, { dialog, _ ->
            Log.e("---", "----click ok---")
            dialog.dismiss()

            //tel and password check
            friend_tel = (v.findViewById(R.id.et_tel) as EditText)?.text?.toString()
            friend_nickname = (v.findViewById(R.id.et_nickname) as EditText)?.text?.toString()
            if (CommonUtils.instance().checkTelephone(friend_tel)) {
                CommonUtils.instance().showShortMessage(mRv, "电话号码无效")
                return@setPositiveButton
            }
            if (CommonUtils.instance().checkNickName(friend_nickname)) {
                CommonUtils.instance().showShortMessage(mRv, "备注／昵称  不得大于7个字符")
                return@setPositiveButton
            }
            skipFirstIn = Result.success("")
            mState = Operation.ADD
            startToRefresh()
            OperationObserveable.onClick()

        })
        mDg_AddFriend.setNegativeButton(R.string.cancel, { dialog, _ ->
            Log.e("---", "----click cancel---")
            dialog.dismiss()
        })
        mDg_AddFriend.create().show()
    }
}