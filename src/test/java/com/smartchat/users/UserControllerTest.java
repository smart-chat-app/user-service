/*
package com.smartchat.users;

import com.smartchat.users.service.users.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = UserController.class)
@Import(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private UserService userService;

    @Test
    void me_shouldReturnUser() {
*/
/*        UserDocument doc = new UserDocument("u123", "user_u123", "Tester");
        Mockito.when(userService.getOrCreateByUserId("u123")).thenReturn(Mono.just(doc));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().jwt(jwt -> jwt.subject("u123")))
                .get().uri("/users/me")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.userId").isEqualTo("u123");
    }*//*

    }
}
*/
