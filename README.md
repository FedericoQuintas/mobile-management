## Technical Details

* The service is written in Java and uses Spring Boot webflux as an underlying framework.
* Even when the scale of the challenge is rather small, I make use of Reactor considering that the company makes use of reactive tools.
* PostgresSQL is the database and the migration is automatically applied as defined in _schema.sql_.

### Code Structure

The service strives to follow Domain Driven Design and the clean architecture approach:

![clean architecture diagram](docs/clean_architecture.png)

(source: https://medium.com/swlh/clean-architecture-a-little-introduction-be3eac94c5d1)


### Testing
I split the testing strategy in 3:
1) Unit tests for the Domain, only interactions with ports are mocked.
2) Integration tests for HTTP: A Test Spring application is booted in order to test http requests. The services covering use cases are mocked.
3) Integration tests for Postgres: With testcontainers I test the interaction a test PostgreSQL instance via the repository.

I skipped end-to-end integration tests but I'd consider them for the most critical paths in a real-life scenario.

### System requirements

In order to build and run this project, you need the following components:

- Java 17
- Docker

## Running the service
1) ./gradlew build _(depending on the environment can take several seconds)_
2) docker-compose up
3) It's possible to interact with the API via Swagger: http://localhost:8080/webjars/swagger-ui/index.html

## Secondary considerations
1) Fonoapi is down and I haven't found any feasible alternative with required data. Therefore I added hard-coded dummy data to
the in-memory repository.
2) I purposely let a user return a device booked by another one, in my experience it's a possible scenario at the office.
3) _ReturnCommandHandler_ and _BookCommandHandler_ are similar and I considered merging them. At the end I opted to leave them separated
understanding that a reasonable v2 of this service could consist in more than one device of each model; in that case, both use cases will diverge.
That being said, I acknowledge that either way would correct with respective tradeoffs.
4) I skip authentication/authorization, although it's a reasonable requirement for the application since the early stages.







