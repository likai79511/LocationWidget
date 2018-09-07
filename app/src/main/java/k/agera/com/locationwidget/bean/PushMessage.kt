package k.agera.com.locationwidget.bean

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
data class PushMessage(var audience: Audience,
                        var message: Message,
                        var platform: String = "all")


data class Audience(var alias: Array<String>)

data class Message(var msg_content: String,
                   var content_type: String,
                   var title: String)       //tel who the message from