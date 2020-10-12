# MeetingCounter
An application for easily creating and organise meetings with automatically counting participants.

Link with MVP:
http://meeting-counter.online/

To build the project following requirements should be met:
* Installed Oracle JDK 11

Internally I use:
* In-memory h2 database for easy start 

To assemble JAR, in the project root directory run:
```bash
mvn clean package
```

Build generate following artifacts:
- executable jar file BulletinBoard-1.0-SNAPSHOT.jar
- Api documentation in target/apidocs


To run execute 
```bash
java -jar target/MeetingCounter_2-1.0-SNAPSHOT.jar
```
