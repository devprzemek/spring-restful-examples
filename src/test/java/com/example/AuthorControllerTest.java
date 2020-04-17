package com.example;

import com.example.model.Author;
import com.example.repository.AuthorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorControllerTest {

    @MockBean
    private AuthorRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        Author author = new Author("John", "Smith", 22);
        when(repository.findById(1L)).thenReturn(Optional.of(author));
    }

    @WithMockUser(username = "USER", password = "user")
    @Test
    public void findLoginOk() throws Exception {
        mockMvc.perform(get("/authors/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Smith")))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    public void findNoLogin() throws Exception {
        mockMvc.perform(get("/authors/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
