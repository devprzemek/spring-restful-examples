package com.example.controller;

import com.example.exceptionhandler.AuthorNotFoundException;
import com.example.model.Author;
import com.example.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class AuthorController {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/authors")
    public List<Author> getAll(){
        return authorRepository.findAll();
    }

    @GetMapping("/authors/{id}")
    public Author getOneAuthor(@PathVariable @Min(1) Long id){
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @PostMapping("/authors/add")
    public Author addAuthor(@RequestBody @Valid Author author){
        return authorRepository.save(author);
    }


    @PutMapping("/authors/{id}")
    public Author saveOrUpdate(@PathVariable Long id, @RequestBody Author author){
        return authorRepository.findById(id).map(element -> {
                                                 element.setName(author.getName());
                                                 element.setSurname(author.getSurname());
                                                 element.setAge(author.getAge());
                                                 return authorRepository.save(element);
                                                 })
                                                 .orElseGet(() -> {
                                                     author.setId(id);
                                                     return authorRepository.save(author);
                                                 });
    }

    @DeleteMapping("/authors/{id}")
    public void deleteAuthor(@PathVariable Long id){
        authorRepository.deleteById(id);
    }
}
