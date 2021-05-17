package com.switchfully.spectangular;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerEndToEndTest {

    @Autowired
    private UserRepository userRepository;
    private CreateUserDto createUserDto;

    @BeforeEach
    private void setUp() {
        createUserDto = new CreateUserDto()
                .setFirstName("Test")
                .setLastName("McTestFace")
                .setProfileName("T3sty")
                .setEmail("test@mail.com")
                .setPassword("P@ssw0rd")
                .setRole(Role.COACHEE.name());
    }

    @LocalServerPort
    private int port;

    @Test
    void createUser_whenCalled_thenOneMoreUserIsPresent() {
        //GIVEN
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        int originalSize = userRepository.findAll().size();
        //WHEN
        WebTestClient.ResponseSpec response = testClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(createUserDto), CreateUserDto.class)
                .exchange();
        //THEN
        response.expectStatus().isCreated();
        assertThat(userRepository.findAll()).hasSize(originalSize + 1);
    }
}
