package com.example.springdatabasevalidation.controller;

import com.example.springdatabasevalidation.exceptionhandler.AuthorNotFoundException;
import com.example.springdatabasevalidation.model.Author;
import com.example.springdatabasevalidation.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.Optional;

@RestController
@Validated
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @PostMapping("/authors/add")
    public Author addAuthor(@RequestBody @Valid Author author){
        return authorRepository.save(author);
    }

    @GetMapping("/authors/{id}")
    public Author getAuthor(@PathVariable @Max(1) Long id){
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }
}
