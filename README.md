# meeting-manager
A very basic meeting app.

##[Server Side](https://github.com/muhammedf/meeting-manager/tree/master/meeting-manager-server)

###Used Technologies
* **Spring Boot**
* **RESTful**
* **JPA (Hibernate)**
* **Maven**
* **H2**
* **Mockito**
* **Assertj**

###RESTful Api
* GET to "/{domain}/{id}": Returns the entity that id represents.
* POST to "/{domain}/": Creates entity with the submitted data.
* PUT to "/{domain}/{id}": Updates the entity that id represents with the submitted data.
* DELETE to "/{domain}/{id}": Deletes the entity that id represents.
* GET to "/{domain}/": Returns all entities for related domain.

domanin = meeting, department, employee


* PUT to "/{domain}/{id}/{secondarydomain}/{id2}": Adds the entity that id2 represents to the entity that id represents as relation.
* DELETE to "/{domain}/{id}/{secondarydomain}/{id2}": Removes the entity that id2 represents from the entity that id represents as relation.
* GET to "/{domain}/{id}/{secondardomain}/": Returns all relational entities for related domain and secondarydomain.

domain == meeting and then secondarydomain = department

domain == department and then secondarydomain = emmployee


###Setting Up
Run this command:

    mvn spring-boot:run

and then server is up at port 8080.

To run tests:

    mvn test

H2 is embedded and runs in cache. If you don't want to lose your data when server shuts down, configure application to your needs.


##[Client Side](https://github.com/muhammedf/meeting-manager/tree/master/meeting-manager-client)

###Used Technologies

* **React**
* **PrimeReact**
* **FontAwesome**
* **SuperAgent**

###Setting Up

To run the app in development mode:

    npm start
    
The app will be available at [http://localhost:3000](http://localhost:3000).
    
To build the app for production:

    npm run build