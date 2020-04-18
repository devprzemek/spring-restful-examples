package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SpringRestfulApplicationTests {

    @Autowired
    private EventPlanner eventPlanner;

    @Test
    void shouldNotCreateEvent() {
        assertThrows(ConstraintViolationException.class, () -> {
            eventPlanner.createAuthorConference(LocalDateTime.now(), LocalDateTime.of(2020, 3, 18, 17, 0, 0));
        });
    }

}
