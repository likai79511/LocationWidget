package k.agera.com.locationwidget.bean

import android.support.annotation.Keep

/**
 * Created by Agera on 2018/9/6.
 */

/*{
    "platform": "all",
    "audience": {
    "alias": [
    "12345678901"
    ]
},
    "message": {
    "msg_content": "Hi,JPush",
    "content_type": "text",
    "title": "msg"
}
}*/
@Keep
data class PushMessage(var audience: Audience,
                        var message: Message,
                        var platform: String = "all")

@Keep
data class Audience(var alias: Array<String>)
@Keep
data class Message(var msg_content: String,
                   var content_type: String,
                   var title: String)       //tel who the message from