package com.example;

import com.example.model.Author;
import com.example.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDatabaseValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDatabaseValidationApplication.class, args);
    }


    @Bean
    CommandLineRunner initDatabase(AuthorRepository repository){
        return args -> {
            repository.save(new Author("Will", "Sheer", 25));
            repository.save(new Author("Andrew", "Prizemore", 25));
        };
    }

}
