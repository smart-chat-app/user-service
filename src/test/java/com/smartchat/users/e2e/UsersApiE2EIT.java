package com.smartchat.users.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.model.Notification;
import com.smartchat.users.model.User;
import com.smartchat.users.service.notification.NotificationService;
import com.smartchat.users.service.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/users-test",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost/jwks",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
@AutoConfigureMockMvc
class UsersApiE2EIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private NotificationService notificationService;

    @Test
    void getMeReturnsUserProfile() throws Exception {
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername("alex");
        user.setDisplayName("Alex");

        when(userService.retrieveCurrentUserInformations()).thenReturn(user);

        mockMvc.perform(get("/users/me")
                        .header("X-User-Id", user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.username").value("alex"))
                .andExpect(jsonPath("$.displayName").value("Alex"));
    }

    @Test
    void sendNotificationReturnsOkResponse() throws Exception {
        String senderId = UUID.randomUUID().toString();
        Notification notification = new Notification();
        notification.setSenderUsername("alex");
        notification.setReceiverUsername("sam");

        doNothing().when(notificationService).sendNotification(any(Notification.class));

        mockMvc.perform(post("/users/send/notification/{senderuuId}", senderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.method").value("OK"));
    }
}
