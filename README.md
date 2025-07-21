# Order Service

![Java](https://img.shields.io/badge/Java-21-brightgreen)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.7-blue)
![Maven](https://img.shields.io/badge/Maven-3.9.9-yellow)

This project is a microservice for order management developed using Spring Boot and built with Maven.

## Features

- **Order Management:** Create, delete, list and match orders & list assets.
- **Authentication & Authorization:** Secure endpoints with user roles using JWT and Spring Security.
- **RESTful API:** Clean and efficient RESTful services.
- **Layered Architecture:** Enum, DTO, Exception, Handler, Filter, Service, Repository, and Utility layers.
- **Database:** H2 in memory database.

## Requirements

- Java 21
- Maven 3.9.9

## Installation

1. Clone the project to your local machine:

    ```bash
    git clone <your-repo-url>
    ```

2. A valid secret key is already set on the application properties for local testing. If you want to use a new secret key, edit the `application.properties` file and add your JWT secret key and password:

    ```properties
    token.signing.key=SECRET_KEY
    token.refresh.signing.key=SECRET_KEY
    ```

4. An Admin user and Customer role user are already created in the database for testing purposes. They also have valid passwords hashed with BCrypt. You can add more users if you need. Existing username and password values are below:
   ```properties
    admin.username=admin
    admin.password=admin123
    admin.customerId=1
    customer.username=canan
    customer.password=canan123
    customer.customerId=2
    ```
   
5. TRY and ING assets are already created in the database for each customer for testing purposes. You can add more assets if you need. Existing asset values are below:
   ```sql
    INSERT INTO assets (id, customer_id, asset_name, size, usable_size)VALUES(1, 2, 'TRY', 100000, 100000),(2, 2, 'ING', 100, 100),(3, 1, 'TRY', 100000, 100000),(4, 1, 'ING', 100, 100);
    ```
   

## Usage

1. **Generate Token:**
   ```http
   POST /api/v1/auth/login
   ```
2. **Request Body:**

   ```json
    {
    "username": "yourUsername",
    "password": "yourPassword"
    }
   ```
3. **Response:**

   ```json
    {
    "token": "TOKEN",
    "refreshToken": "REFRESH_TOKEN",
    "expiresAt": "EXPIRES_AT"
    }
   ```   
4. **Access Secure Endpoint:**
     ```http
     POST /api/v1/orders
     ```
   **HEADER**
   ```http
   Authorization: Bearer TOKEN
   ```  
   **REQUEST**
   ```json
   {
     "customerId": 1,
     "assetName": "ING",
     "side": "BUY",
     "size": 100,
     "price": 25.5
   }

   ```  
   **RESPONSE**
   ```json
   {
     "id": 9007199254740991,
     "customerId": 9007199254740991,
     "assetName": "string",
     "side": "BUY",
     "size": 0,
     "price": 0,
     "status": "PENDING",
     "createDate": "2025-07-21T00:58:29.065Z",
   }
   ```

## Documentation
For detailed API documentation, refer to the [OpenAPI Specification](https://swagger.io/specification/) available at `/v3/api-docs` or view it interactively at `/swagger-ui/index.html`.

## Running the Application
Start the application with the following command:
```bash
./mvnw spring-boot:run
```
or
```bash
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

## Project Structure
- `src/main/java/com/example/` - Application source code
- `src/main/resources/` - Configuration and static files
- `src/test/java/com/example/` - Tests

## Testing
To run tests:
```bash
./mvnw test
```

## Configuration
Edit `src/main/resources/application.properties` for application settings.

## Contribution
Feel free to submit a pull request to contribute.

