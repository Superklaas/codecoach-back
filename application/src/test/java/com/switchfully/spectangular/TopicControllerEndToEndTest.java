package com.switchfully.spectangular;

import com.switchfully.spectangular.dtos.TopicDto;
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
public class TopicControllerEndToEndTest {
    @LocalServerPort
    private int port;

    @Test
    @Sql("/sql/testSetup.sql")
    void getAllTopics_whenCalled_thenAllTopicsAreFound() {
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
        TopicDto[] topicDtos = given()
                .header("Authorization", (bearerToken == null) ? "" : bearerToken)
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/topics")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TopicDto[].class);

        assertThat(topicDtos).hasSize(1);
    }
}
