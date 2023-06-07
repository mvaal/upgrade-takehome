# Back-end Tech Challenge
An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.

The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for design and development of a REST
API service that will manage the campsite reservations.

## Design

Design docs can be found [here](./design/Design.md).

## Running

Running local requires the `local` spring profile. This is already set up for you if you decide to run with the below options.

Different jpa source should be used to run in a more production like environment.

### IntelliJ
#### Ephemeral 

> Run the [Campsite Reservation Service](./.run/Campsite%20Reservation%20Service.run.xml) configuration.

#### Persistent

> Run the [Campsite Reservation Service - Persistent](./.run/Campsite%20Reservation%20Service%20-%20Persistent.run.xml) configuration.

### Docker
#### Ephemeral

    make docker

#### Persistent

    make docker-persistent

## Usage
### Swagger

> http://localhost:8080/swagger-ui.html

### booking-service.http

Pre-populated sample queries can be found [here](booking-service.http).

### H2 Console

> http://localhost:8080/h2-console

#### Connection Info
##### Ephemeral
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:mydb
User Name: upgrade
Password: password
```
##### Persistent
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:file:<working directory>/upgrade-takehome/data/volcanocampsite
User Name: upgrade
Password: password
```
##### Sample Query
```sql
select EMAIL, FULL_NAME, STAY_DATE, COST from CAMPER c, BOOKING b, RESERVATION r where c.ID = b.CAMPER_ID and b.ID = r.BOOKING_ID
```

# References
* https://docs.spring.io/spring-boot/docs/3.1.x/reference/html/features.html