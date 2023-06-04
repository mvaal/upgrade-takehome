# Back-end Tech Challenge
An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.

The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for design and development of a REST
API service that will manage the campsite reservations.

To streamline the reservations a few constraints need to be in place -
* The campsite will be free for all.
* The campsite can be reserved for max 3 days.
* The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
* Reservations can be cancelled anytime.
* For sake of simplicity assume the check-in & check-out time is 12:00 AM



http://localhost:8080/swagger-ui.html
http://localhost:8080/actuator/prometheus
http://localhost:8080/h2-console
```
jdbc:h2:mem:mydb
upgrade
password
```
```sql
select EMAIL, FULL_NAME, DATE, COST from CAMPER c, BOOKING b, RESERVATION r where c.ID = b.CAMPER_ID and b.ID = r.BOOKING_ID
```

# References
* https://docs.spring.io/spring-boot/docs/3.1.x/reference/html/features.html