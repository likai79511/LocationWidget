# LocationWidget
The little tools that can get self or friends location.


This tools will use push to send custom message to other specific users(friends),the message will carry a type and userID.

The type is present specific purpose,such as request a location or receive a location.

The userID as user identification,present the message is from.



So actually client will play two roles.

As a Receiver: receiver message from others.can to do next action accoriding message type. for example, if this message purpose 

is get location, client will get location by AMap and then take location data send to message's from(userID).

As a Sender: send message to others, initially just used to request location.



Becuase client include receiver and sender. can send message to others to notify them get  location and then 

return data back. 

such as request a location or receive location from others.




Technical metrics:

Develop language: Kotlin

Event Driver/Pattern: Agera framework(Observe pattern)

Daemon: Jobscheduler

Push: JPush

DB + Server: Bomb

Location: AMap(Gao De Map)

Splash-Animation: Vector(SVG)

