cfollow
=======

NOTE: This repository was used for the development of my bachelor's thesis. Data on wiki pages may be out of date. Someday I might fix it and make something out of this project. In the meanwhile, you can do whatever you want with it.

A working demo of the online system **MAY** be available online at:

    URL (website): https://ivo.qa/f
    URL (webservice): https://ivo.qa/follow

You can use the following credentials for login:

    Username: github
    Password: github
    
(The service only enforces strong passwords from the userRegister API endpoint and not the createUser admin API).

Cfollow is a vehicle tracking solution created with the idea to aid finding of stolen vehicles.

Cfollow consists of three main parts.
 - The client - a mobile device running the Linux operating system that uses GPS for geographic location and WiFi to connect to wireless networks to share data and accept instructions from a Cfollow server.
 - The server - an application that communicates with clients and stores provided data
 - The web application - a service where users (vehicle owners) can register their clients and track their vehicles

In brief, the client is in hibernated mode for most of the time and is "awaken" at specific time interval to carry out its procedure. During its procedure, it will record current GPS location and attempt to connect to an open WiFi network. The device is built with the presumption that free WiFi is now widely spread and available through city centres, gas stations and shopping centres. The client records location and connection data to an internal database as WiFi is not expected to be available every time the device executes its routine. By default, the client will sync only the latest location and connection data to the server, but the server may indicate it requires additional information.

A flowchart of the client's routine is available [in the Wiki](https://github.com/ivaivalous/cfollow/wiki/Cfollow-client-workflow).

The server, through responses to client requests, is able to indicate it needs additional data, modify configuration settings and execute additional code. The server is the means of the website user (hence vehicle owner) to express their will. 

Client-server communication is explained [in the Wiki](https://github.com/ivaivalous/cfollow/wiki/Communications-Design).

The website is where users are given a friendly way to access their information and define the client's behavior.

Please note the project is currently in development and is far from complete. 

This is the BEng project of Ivaylo Marinkov.
