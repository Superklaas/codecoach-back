package com.switchfully.spectangular;

import com.switchfully.spectangular.dtos.UserDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerEndToEndTest {

    @LocalServerPort
    private int port;

    @Test
    void createUser_whenCalled_thenOneMoreUserIsPresent() {
        //GIVEN
        String requestBody = """
                {
                  "firstName": "Testa",
                  "lastName": "McTestFace",
                  "profileName": "Test123",
                  "email": "test@mail.com",
                  "password": "WachtW00rd",
                  "role": "Coachee"
                }""";
        //WHEN
        Response postResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/users");
        //THEN
        postResponse
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(UserDto.class);
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void createUser_whenCalledWithExistingEmail_thenStatusCodeIsBadRequest() {
        //GIVEN
        String requestBody = """
                {
                  "firstName": "Testa",
                  "lastName": "McTestFace",
                  "profileName": "Test123",
                  "email": "test@spectangular.com",
                  "password": "WachtW00rd",
                  "role": "Coachee"
                }""";
        //WHEN
        Response postResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/users");
        //THEN
        postResponse
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void getUserById_whenCalled_thenOneUserIsFound() {
        //GIVEN
        Response postResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/authenticate")
                .body("{\"username\":\"test@spectangular.com\",\"password\":\"YouC0ach\"}")
                .post();

        String bearerToken = postResponse
                .header("Authorization");
        //WHEN
        //THEN
        given()
                .header("Authorization", (bearerToken == null) ? "" : bearerToken)
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/users/{id}", "1000")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void updateToCoach_whenCalled_thenUserIsUpdated() {
        //GIVEN
        Response postResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/authenticate")
                .body("{\"username\":\"test@spectangular.com\",\"password\":\"YouC0ach\"}")
                .post();

        String bearerToken = postResponse
                .header("Authorization");
        //WHEN
        //THEN
        UserDto userDto = given()
                .header("Authorization", (bearerToken == null) ? "" : bearerToken)
                .baseUri("http://localhost")
                .port(port)
                .when()
                .post("/users/{id}/coachify", "1000")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);

        assertThat(userDto.getRole()).isEqualTo("COACH");
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void getAllCoaches_whenCalled_thenAllCoachesAreFound() {
        //GIVEN
        Response postResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/authenticate")
                .body("{\"username\":\"test@spectangular.com\",\"password\":\"YouC0ach\"}")
                .post();

        String bearerToken = postResponse
                .header("Authorization");
        //WHEN
        //THEN
        UserDto[] userDtos = given()
                .header("Authorization", (bearerToken == null) ? "" : bearerToken)
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/users/coaches")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto[].class);

        assertThat(userDtos).hasSize(1);
        assertThat(userDtos).allSatisfy(userDto -> userDto.getRole().equals("COACH"));
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void sendResetToken_whenCalled_thenAnEmailIsSent() {
        //GIVEN
        //WHEN
        Response response = given()
                .header("Email", "test@spectangular.com")
                .body("http://localhost:8080")
                .baseUri("http://localhost")
                .port(port)
                .when()
                .post("/users/forgot-password");
        //THEN
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void sendResetToken_givenNonExistentEmail_thenStatusCodeIsBadRequest() {
        //GIVEN
        //WHEN
        Response response = given()
                .header("Email", "fake@fake.com")
                .body("http://localhost:8080")
                .baseUri("http://localhost")
                .port(port)
                .when()
                .post("/users/forgot-password");
        //THEN
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void resetPassword_givenNonExistentEmail_thenStatusCodeIsBadRequest() {
        //GIVEN
        String validToken = "cba90cb7-4ef5-4fcb-a4a1-6d7177ce75e8";
        //WHEN
        Response response = given()
                .body("Y0uCoach")
                .baseUri("http://localhost")
                .port(port)
                .when()
                .post("/users/reset-password?token={token}", validToken);
        //THEN
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Sql("/sql/testSetup.sql")
    void resetPassword_givenNonExistentResetToken_thenStatusCodeIsBadRequest() {
        //GIVEN
        String invalidToken = "cba90cb7-4ef5-4fcb-a4a1-6d7177ce75e9";
        //WHEN
        Response response = given()
                .body("Y0uCoach")
                .baseUri("http://localhost")
                .port(port)
                .when()
                .post("/users/reset-password?token={token}", invalidToken);
        //THEN
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
