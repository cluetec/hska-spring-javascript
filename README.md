# Showcase HSKA Praxisforum
 
This is the sample todo application used in the presentation "Modern web applications with Spring Boot, REST and JavaScript" at HS Karlsruhe on 17.06.2015.
 
## Setup
 
* Standalone Java application
* Spring-based backend with RESTful API
* Standalone JavaScript Frontend with AngularJS
 
To keep getting started simple, we are using the backend server to also serve the built version of the JavaScript client.
 
## Running
 
* Download, install and run a [mongoDB](https://www.mongodb.org/) instance on your local machine.
* To build the application, just run `mvn clean package`.
* The resulting jar can simply be started via `java -jar target/hska-showcase-1.0.0-SNAPSHOT.jar`.
* Start your browser and navigate to [http://localhost:8088/](http://localhost:8088/)
