package pers.fjl.healthcheck;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.healthcheck.service.UserService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HealthCheckApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateAccount() {
        userService.deleteUserIfExists("fjl@example.com");
        String requestBody = "{\"first_name\": \"Jiale\", \"last_name\": \"Fang\", \"username\": \"fjl@example.com\", \"password\": \"123456\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v1/user")
                .then()
                .statusCode(201)
                .body("first_name", equalTo("Jiale"))
                .body("last_name", equalTo("Fang"))
                .body("username", equalTo("fjl@example.com"));

        given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("fjl@example.com", "123456")
                .when()
                .get("/v1/user/self")
                .then()
                .statusCode(200)
                .body("first_name", equalTo("Jiale"))
                .body("last_name", equalTo("Fang"))
                .body("username", equalTo("fjl@example.com"));
    }

    @Test
    public void testUpdateAccount() {
        // Assuming you have an endpoint for updating user information
        String requestBody = "{\"first_name\": \"Jayden\", \"last_name\": \"F\", \"password\": \"12345678\"}";

        given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("fjl@example.com", "123456")
                .body(requestBody)
                .when()
                .put("/v1/user/self")
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("fjl@example.com", "12345678")
                .when()
                .get("/v1/user/self")
                .then()
                .statusCode(200)
                .body("first_name", equalTo("Jayden"))
                .body("last_name", equalTo("F"))
                .body("username", equalTo("fjl@example.com"));
    }

}
