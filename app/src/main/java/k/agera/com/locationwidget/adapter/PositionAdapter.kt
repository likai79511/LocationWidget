package k.agera.com.locationwidget.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import k.agera.com.locationwidget.MyApp
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.utils.AppendList

/**
 * Created by Agera on 2018/8/27.
 */
class PositionAdapter : RecyclerView.Adapter<PositionAdapter.VH>() {

    var userList:ArrayList<String> = AppendList<String>().add("${MyApp.instance().selfAlias}-自己").compile()


    override fun onBindViewHolder(holder: VH?, position: Int) {
        var userInfo = userList[position].split("-")
        holder?.tv?.text = "${userInfo[0]} [${userInfo[1]}]"
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        return VH(View.inflate(MyApp.instance(), R.layout.item_friend, null))
    }


    class VH(contentView: View) : RecyclerView.ViewHolder(contentView) {
        var tv: TextView = contentView.findViewById(R.id.tv) as TextView

    }

    fun addFriend(friend: String) {
        userList.add(friend)
    }

    fun removeFriend(friend: String) {
        userList.remove(friend)
    }

}