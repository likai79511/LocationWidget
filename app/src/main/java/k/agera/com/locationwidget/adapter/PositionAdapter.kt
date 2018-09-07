package k.agera.com.locationwidget.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.location.MapActivity
import k.agera.com.locationwidget.utils.AppendList
import k.agera.com.locationwidget.utils.CommonUtils

/**
 * Created by Agera on 2018/8/27.
 */
class PositionAdapter(var ctx: Context) : RecyclerView.Adapter<PositionAdapter.VH>() {

    var userList: ArrayList<String> = AppendList<String>().add("${MyApp.instance().getSelf()}-自己").compile()

    override fun onBindViewHolder(holder: VH?, position: Int) {
        var userInfo = userList[position].split("-")
        userInfo?.let {
            if (userInfo.size > 1) {
                holder?.tv?.text = "${it[0]} [${it[1]}]"
            } else {
                holder?.tv?.text = "${it[0]}"
            }
            var tel = it[0]
            holder?.root?.setOnClickListener {
                var intent = Intent(ctx, MapActivity::class.java)
                intent.putExtra("data", tel)
                ctx.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        var view = View.inflate(MyApp.instance(), R.layout.item_friend, null);
        var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.instance().dp2px(60f))
        lp.setMargins(CommonUtils.instance().dp2px(10f), CommonUtils.instance().dp2px(10f), CommonUtils.instance().dp2px(10f), CommonUtils.instance().dp2px(10f))
        view.layoutParams = lp
        return VH(view)
    }


    class VH(contentView: View) : RecyclerView.ViewHolder(contentView) {
        var root: CardView = contentView.findViewById(R.id.root) as CardView
        var tv: TextView = contentView.findViewById(R.id.tv) as TextView
    }


    fun removeFriend(friend: String) {
        userList.remove(friend)
    }

    fun setFriendList(friend: String) {
        clearFriendList()
        if (friend != null && !friend.isEmpty()) {
            var fs = friend.split(",")
            fs.forEach {
                userList.add(it)
            }
        }
        notifyDataSetChanged()
    }


    fun clearFriendList() {
        userList.clear()
        userList.add("${MyApp.instance().getSelf()}-自己")
    }

    fun getFriendList() = userList
}