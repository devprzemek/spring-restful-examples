package com.example;

import com.example.constraints.ConsistentDataParameters;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Validated
public class EventPlanner {

    @ConsistentDataParameters
    public void createAuthorConference(LocalDateTime begin, LocalDateTime end){
        //logic creating conference
        System.out.println("ok");
    }
}
