# Price Comparator

## General Description

Backend Solution to help users compare prices of everyday grocery items across different supermarket chains<br /> (e.g.,
Lidl, Kaufland, Profi).                                       
The system allows users to find the best deals, set price alerts and manage their shopping lists effectively.

## Implementation Details

You can find the implementation details of the solution by following this: ["**google docs link
**"](https://docs.google.com/document/d/1GxepsYO1hfFOF2726U3GPdVHrpQHFodxqd0RjLn6LPM/edit?usp=sharing).

## Prerequisites

- Use `Java 24` & `Spring Boot 3.5.0` & `Maven`
- `Docker` was used for the database
- Dependencies needed:
    - Spring Boot Web: `spring-boot-starter-web`
    - Spring Data JPA: `spring-boot-starter-data-jpa`
    - PostgreSQL driver: `postgresql`
    - Flyway for PostgreSQL: `flyway-database-postgresql`
    - CSV parsing: `commons-csv`
    - Testing: `spring-boot-starter-test`
    - Less boilerplate: `Lombok`
    - Separate db for tests: `H2`

## Building and running

- Clone the repository via `git clone git@github.com:Iradu15/price-comparator.git`
- `cd` into and run:
    - `docker compose up -d` to start the container for `Postgres`
    - Run the app using `./mvnw spring-boot:run`

- TIP for requests, in `/data` directory you can find:
    - `Postman` dump: `price-comparator.postman_collection.json`
    - For `curl` alternative: `curl.txt`

For further reference, please consider the following sections:

* [Postman documentation](https://learning.postman.com/docs/getting-started/overview/)
* [Curl cheatsheet](https://devhints.io/curl)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

