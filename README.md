cfollow
=======

"Follow" is a vehicle tracking system, consisting of a central server, client devices hidden in vehicles to be tracked, and a web application where vehicle owners can monitor tracking data.
Clients use GPS to record geographic position and send it to the server over WiFi, using a web API. The same web API is used with the web application.

The project was in fact my diploma thesis for graduating as a Computer Engineer (BSc) from the Technical University of Sofia. Thus, some functionality is incomplete but I may work on it in my spare time in the future in order to produce a usable solution.

Things that are (fully) functional:
 - The web API can receive location and logs data from clients using an API token for authorization
 - The web API can login users. Users are vehicle owners that want to track their vehicles and/or admins that can create or edit other users. Authorization is done using a username and a password
 - The web API can provide users with data sent by clients. A user can view the data her Follow device sent out - locations, IP addresses communication was done from, and logs. Authorization is done using a JWT which is sent to the user when she logs in
 - The web API can receive and act on different admin requests - create and modify users
 - The web API can register new users and send out activation email
 - The web API can activate users through their activation token
 - The web API implements user data protection through login security measures
 - The web app can interact with the web API in order to provide login and viewing of data. It can also render location data using Google Maps API
 
Things that are outstanding
 - Documentation update (wiki)
 - Web app has a minimal intefrace that is to be enriched with more CSS eye candy
 - Web app to work with the admin web API
 - Actual Follow client implementation
 - Web API with additional security features

A working demo of the online system **MAY** be available online at:

    URL (website): https://ivo.qa/f
    URL (webservice): https://ivo.qa/follow

You can use the following credentials for login:

    Username: github
    Password: github
    
(The service only enforces strong passwords from the userRegister API endpoint and not the createUser admin API).

You are free to use this project in any way you find useful. 
