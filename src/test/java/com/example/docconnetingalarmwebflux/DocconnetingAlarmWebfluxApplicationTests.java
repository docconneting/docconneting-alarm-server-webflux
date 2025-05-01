package com.example.docconnetingalarmwebflux;

import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestMockConfig.class)
class DocconnetingAlarmWebfluxApplicationTests {

    @MockitoBean
    private FirebaseMessaging firebaseMessaging;

    @Test
    void contextLoads() {
    }

}
