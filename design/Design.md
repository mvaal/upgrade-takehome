# Design
## Constraints
* The campsite will be free for all.
* The campsite can be reserved for max 3 days.
* The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
* Reservations can be cancelled anytime.
* For sake of simplicity assume the check-in & check-out time is 12:00 AM

### Assumptions
* No timezones
* Cost was included in the model, but is always set as 0

## System Requirements
* The users will need to find out when the campsite is available. So the system should expose an API to provide information of the
  availability of the campsite for a given date range with the default being 1 month.
* Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite
  along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
* The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow
  modification/cancellation of an existing reservation
* Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping
  date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
* Provide appropriate error messages to the caller to indicate the error cases.
* In general, the system should be able to handle large volume of requests for getting the campsite availability
* There are no restrictions on how reservations are stored as long as system constraints are not violated.

### Assumptions
* Users can book multiple times

## Database
![Class Diagram](./ClassDiagram.drawio.svg "Class Diagram")

## Design Decisions
### High Scalability
To support high scalability, the application should scale horizontally and not just vertically.
This also means that concurrency issues should be solved outside the JVM instead of locks in the code.
Concurrency will be handled by the database using transactions and unique keys.

Uniqueness will be enforced by
* Reservation Date - Must be unique
* Camper email - Must be unique