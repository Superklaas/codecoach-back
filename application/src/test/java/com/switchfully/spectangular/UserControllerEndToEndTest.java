package com.switchfully.spectangular;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.repository.UserRepository;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerEndToEndTest {

    @Autowired
    private UserRepository userRepository;

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
    @Sql("/sql/insertUser.sql")
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
    @Sql("/sql/insertUser.sql")
    void getUserById_whenCalled_thenOneUserIsFound() {
        //GIVEN
        User user = userRepository.findByEmail("test@spectangular.com").get();

        Response postResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/authenticate")
                .body("{\"username\":\"test@spectangular.com\",\"password\":\"P@ssw0rd\"}")
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
                .get("/users/{id}", "10")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto.class);
    }

}
