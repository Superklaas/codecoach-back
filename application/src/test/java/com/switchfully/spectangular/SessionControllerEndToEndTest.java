package com.switchfully.spectangular;

import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.dtos.UserDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SessionControllerEndToEndTest {

    @LocalServerPort
    private int port;

    @Test
    @Sql("/sql/insertUser.sql")
    void createSession_whenCalled_thenOneMoreSessionIsPresent(){
        //GIVEN
        LocalDate testDate = LocalDate.now().plusDays(1);

        String requestBody = "{\"subject\":\"spring\"," +
                "\"date\":\"" + testDate + "\"," +
                "\"startTime\":\"12:12:12\", " +
                "\"location\":\"Microsoft Teams\", " +
                "\"coachId\": 2," +
                "\"coacheeId\": 1," +
                "\"remarks\":\"These are remarks.\"}";


        /*String requestBody = """
                {
                    "subject" : "spring",
                    "date" : " """
                + testDate +
                """
                    " ,
                    "startTime": "12:12:12",
                    "location": "Microsoft Teams",
                    "coachId" : 2,
                    "coacheeId": 1,
                    "remarks": ""           
                }
                """;*/

        System.out.println(requestBody);

        Response AuthorizepostResponse = given()
                .baseUri("http://localhost")
                .port(port)
                .basePath("/authenticate")
                .body("{\"username\":\"test@spectangular.com\",\"password\":\"YouC0ach\"}")
                .post();

        String bearerToken = AuthorizepostResponse.header("Authorization");

        //WHEN
        Response postResponse = given()
                .header("Authorization", (bearerToken == null) ? "" : bearerToken)
                .baseUri("http://localhost")
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/sessions");

        //THEN
        postResponse
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(SessionDto.class);
    }

}
