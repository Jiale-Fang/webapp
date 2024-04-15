package pers.fjl.healthcheck;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.healthcheck.dao.UserDao;
import pers.fjl.healthcheck.po.User;
import pers.fjl.healthcheck.service.UserService;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HealthCheckApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    private final CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void init() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    @Test
    public void testCreateAccount() throws InterruptedException {
        latch.await();

        userService.deleteUserIfExists("fjl@example.com");
        String requestBody = "{\"first_name\": \"Jiale\", \"last_name\": \"Fang\", \"username\": \"fjl@example.com\", \"password\": \"123456\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v2/user")
                .then()
                .statusCode(201)
                .body("first_name", equalTo("Jiale"))
                .body("last_name", equalTo("Fang"))
                .body("username", equalTo("fjl@example.com"));

        // Activate account
        userDao.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::isEmailVerified, true)
                .eq(User::getUsername, "fjl@example.com"));

        given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("fjl@example.com", "123456")
                .when()
                .get("/v2/user/self")
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
                .put("/v2/user/self")
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("fjl@example.com", "12345678")
                .when()
                .get("/v2/user/self")
                .then()
                .statusCode(200)
                .body("first_name", equalTo("Jayden"))
                .body("last_name", equalTo("F"))
                .body("username", equalTo("fjl@example.com"));
    }

}
