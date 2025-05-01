package com.example.docconnetingalarmwebflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestMockConfig.class)
class DocconnetingAlarmWebfluxApplicationTests {

    @Test
    void contextLoads() {
    }

}
