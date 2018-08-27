package k.agera.com.locationwidget.utils

/**
 * Created by Agera on 2018/8/27.
 */
class AppendList<T> {

    var list = ArrayList<T>()

    init {
        list.clear()
    }


    fun add(value: T): AppendList<T> {
        list.add(value)
        return this
    }

    fun remove(value: T): AppendList<T> {
        list.remove(value)
        return this
    }

    fun compile(): ArrayList<T> = list

}