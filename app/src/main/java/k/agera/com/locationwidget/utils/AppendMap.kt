package k.agera.com.locationwidget.utils

/**
 * Created by Agera on 2018/6/14.
 */
class AppendMap<K, V> {

    private var map: HashMap<K, V> = HashMap()

    init {
        map.clear()
    }


    fun put(key: K, value: V): AppendMap<K, V> {
        map.put(key, value)
        return this
    }


    fun remove(key: K) {
        map.remove(key)
    }

    fun compile() = map
}