# Maybank Technical Test

Spring Boot Application for Maybank Technical Test

## Getting Started

### Table of Contents
> - [Prerequisites](#prerequisites)
> - [Project Structure](#project-structure)
> - [Details Class](#details-class)
> - [Design Pattern](#design-pattern)
> - [Details Database](#details-database)
> - [Installation](#installation)
> - [API Documentation](#api-documentation)
> - [API Usage](#api-usage)
> - [Built With](#built-with)
> - [Author](#author)
> - [License](#license)

---

### Prerequisites
- Java SDK 17 or above
- Apache Maven 3.8.4 or above
- MySQL Database
- Jasper Report
- Digital Ocean Space
- Amazon S3 SDK
- Github API

---

### Project Structure
```
.
├── docs
│   └── Maybank Technical Test.postman_collection.json
├── src
│   ├── main/java/dev/lukman/maybank
│   │   ├── MaybankApplication.java
│   │   ├── adapter/GithubClient.java
│   │   ├── api/request/RequestParameterSearch.java
│   │   ├── api/request
│   │   │   ├── ErrorResponse.java
│   │   │   └── ResponseSuccess.java
│   │   ├── configuration
│   │   │   ├── HttpClientConfig.java
│   │   │   └── SpaceConfig.java
│   │   ├── constant
│   │   │   ├── GlobalConstant.java
│   │   │   └── ResponseCode.java
│   │   ├── controller/RestGithubController.java
│   │   ├── dto
│   │   │   ├── GithubResponse.java
│   │   │   ├── GithubUserDTO.java
│   │   │   └── ReportHistoryDTO.java
│   │   ├── exception
│   │   │   ├── ErrorException.java
│   │   │   └── ExceptionControllerAdvice.java
│   │   ├── model/ReportHistory.java
│   │   ├── repository/ReportHistoryRepository.java
│   │   └── service
│   │       ├── JasperService.java
│   │       ├── ReportService.java
│   │       └── impl
│   │           ├── JasperServiceImpl.java
│   │           └── ReportServiceImpl.java
│   └── resources
│       ├── application.yml
│       └── templates/github.jrxml
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

### Details Class
- Package `dev.lukman.maybank`
  - adapter
    - `GithubClient.java` - Github API Client
  - api
    - request
      - `RequestParameterSearch.java` - Request Parameter Search
    - response
      - `ErrorResponse.java` - Error Response
      - `ResponseSuccess.java` - Success Response
  - configuration
    - `HttpClientConfig.java` - Http Client Config
    - `SpaceConfig.java` - Space Config
  - constant
    - `GlobalConstant.java` - Global Constant
    - `ResponseCode.java` - Response Code Constant
  - controller
    - `RestGithubController.java` - Rest Github Controller
  - dto
    - `GithubResponse.java` - Github Response From API
    - `GithubUserDTO.java` - Github User DTO
    - `ReportHistoryDTO.java` - Report History DTO
  - exception
    - `ErrorException.java` - Error Exception
    - `ExceptionControllerAdvice.java` - Exception Controller Advice
  - model
    - `ReportHistory.java` - Report History Model
  - repository
    - `ReportHistoryRepository.java` - Report History Repository
  - service
    - `JasperService.java` - Jasper Service
    - `ReportService.java` - Report Service
    - impl
      - `JasperServiceImpl.java` - Jasper Service Impl
      - `ReportServiceImpl.java` - Report Service Impl
  - `MaybankApplication.java` - Main Class

---

### Design Pattern

This project follows a number of software design patterns, which help in maintaining a well-structured and scalable codebase. Below are the key design patterns identified:
1. #### Layered (N-tier) pattern:
   - This is an architectural pattern which can be seen in the separation of responsibilities into distinct layers such as controller, service, repository, etc. Each layer has specific roles and responsibilities, providing a clean separation of concerns.
      - Controller Layer (controller): `RestGithubController.java` is part of this layer. This layer is responsible for handling HTTP requests and responses.
      - Service Layer (service): `JasperService.java` and `ReportService.java` are part of this layer. This layer encapsulates the application's business logic, controlling transactions and coordinating responses in the implementation.
      - Repository Layer (repository): `ReportHistoryRepository.java` is part of this layer. This layer is responsible for providing access to data stored in databases or other types of storage systems.
      - Model Layer (model): `ReportHistory.java` is part of this layer. This layer represents data and the rules that govern access to and updates of this data.
      - DTO Layer (dto): `GithubResponse.java`, `GithubUserDTO.java`, `ReportHistoryDTO.java` are part of this layer. Data Transfer Objects are used to transfer data between processes, reducing the number of method calls.
2. ####  Adapter pattern: 
   - `GithubClient.java` could be seen as an adapter for Github's API. It's the piece of code that is directly responsible for making requests to Github's API and might handle translating between the application's models and the API's models.
3. #### Singleton pattern: 
   - The `HttpClientConfig.java`, `SpaceConfig.java`, `GlobalConstant.java`, `ResponseCode.java` classes are potential candidates for the singleton pattern. If these classes hold application-wide, shared resources (such as configuration parameters), then they could be implemented as singletons.
4. #### Factory pattern: Factory pattern: 
   - While it's not directly listed, if there's a need to create objects of `GithubResponse.java`, `GithubUserDTO.java`, `ReportHistoryDTO.java` based on some logic or condition, then a factory pattern might be beneficial.
5. #### Strategy pattern: 
   - It's not explicitly stated, but depending upon how the `JasperService` and `ReportService` are used, they might benefit from the strategy pattern. For example, if there are different types of reports or different ways to generate reports, the strategy pattern could be used to switch between these at runtime.
6. #### Exception Handling pattern: 
   - `ErrorException.java` and `ExceptionControllerAdvice.java` are part of this pattern. The usage of exception handling provides a way to handle both expected and unexpected errors gracefully.

---

### Details Database
- Database Name: `maybank`
- Table Name: `report_history`
- Table Structure:
  - `id` - `BIGINT(20)` - `PRIMARY KEY`
  - `file_id` - `VARCHAR(255)`
  - `parameter` - `VARCHAR(255)`
  - `totalData` - `VARCHAR(255)`
  - `created_at` - `TIMESTAMP`

---

### Installation

1. Clone the repo `git clone https://gitlab.com/iNitial-M/maybank-technical-test.git`
2. Install Maven Dependencies `mvn clean install`
3. Configure properties in `application.yml` file
    ```yml
    spring:
      datasource:
          url: jdbc:mysql://<your-database-host>/maybank # The URL of your MySQL database
          username: <your-username> # The username of your MySQL database
          password: <your-password> # The password of your MySQL database
      jpa:
          hibernate:
            naming:
                implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
                physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
            ddl-auto: update # (Optional) This option automatically updates the database schema to match your entities.
          properties:
            hibernate:
                dialect: org.hibernate.dialect.MySQL8Dialect # This option specifies the SQL dialect of your database
                format_sql: true # (Optional) This option pretty-prints SQL statements in the console

    api:
      github:
        token: <your-github-token> # Your GitHub API token

    do:
      space:
          key: <your-space-key> # Your DigitalOcean Space access key
          secret: <your-space-secret> # Your DigitalOcean Space secret access key
          endpoint: <your-space-endpoint> # The endpoint of your DigitalOcean Space
          region: <your-space-region> # The region where your DigitalOcean Space resides
          bucket: <your-space-bucket> # The name of your DigitalOcean Space bucket
    ```
4. Run test `mvn test`
5. Run the application `mvn spring-boot:run`

---

### API Documentation
- [Postman Collection](docs/Maybank Technical Test.postman_collection.json)

### API Usage
1. #### Report Search
   - Method: `GET`
   - URL: `/v1/report`
   - Query Parameters:
     - `search` (Required: `true`) - Allows the search of users and displays the results in a PDF.
     - `sort` (Required: `false`) - Sorts the results of your query by number of `followers` or `repositories`, or by the date the user `joined` GitHub.
     - `order` (Required: `false`) - Determines whether the first search result returned has the highest number of matches (`desc`) or lowest number of matches (`asc`).
     - `page` (Required: `false`) - Page number of the results to fetch.
     - `size` (Required: `false`) - The number of results per page (`max 100`).
   - Description: Searches for reports based on specific criteria (sort=`desc`, order=`repository`, page=`0`, size=`max 100`).
2. #### List History Report 
   - Method: `GET`
   - URL: `/v1/report/history`
   - Description: Retrieves a list of report history.
3. #### Download Report History
   - Method: `GET`
   - URL: `/v1/report/download`
   - Query Parameters:
     - `id` (Required: `true`) - Allows clients to download history export data with specific file id.
   - Description: Downloads the report history based on a specific id.
---

### Built With
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Web](https://spring.io/guides/gs/serving-web-content/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Validation](https://spring.io/guides/gs/validating-form-input/)
- [Amazon S3](https://aws.amazon.com/s3/)
- [Digital Ocean Space](https://www.digitalocean.com/products/spaces/)
- [Jasper Report](https://community.jaspersoft.com/project/jasperreports-library)
- [MySQL](https://www.mysql.com/)
- [Maven](https://maven.apache.org/)
- [Lombok](https://projectlombok.org/)
- [Github API](https://docs.github.com/en/rest)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)

---

### Author
- [M Lukman Nor Khakim](https://www.linkedin.com/in/intial-m503/)
- [Email](mailto:initial.m503@gmail.com)
- [Phone](tel:+6281359124868)
- [WhatsApp](https://wa.me/6281359124868)
- [Instagram](https://www.instagram.com/m_lukmaannn/)
- [Twitter](https://twitter.com/initial_m503)

---

### License
Distributed under the MIT License. See [LICENSE](LICENSE) for more information.