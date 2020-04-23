package com.example;

import com.example.model.Author;
import com.example.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private AuthorRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void init(){
        Author author = new Author("John", "Smith", 22);
        author.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(author));
    }

    @WithMockUser(username = "user", password = "user")
    @Test
    public void find_login_ok() throws Exception {
        mockMvc.perform(get("/authors/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John"))) // when array is returned -> "$[0].name"
                .andExpect(jsonPath("$.surname", is("Smith")))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @Test
    public void save_withLogin_ok() throws Exception {
        Author author = new Author("Steward", "Barn", 35);
        author.setId(2L);
        when(repository.save(any(Author.class))).thenReturn(author);

        mockMvc.perform(post("/authors/add").content(mapper.writeValueAsString(author)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Steward"))) // when array is returned -> "$[0].name"
                .andExpect(jsonPath("$.surname", is("Barn")))
                .andExpect(jsonPath("$.age", is(35)));

    }

    @Test
    public void find_no_login() throws Exception {
        mockMvc.perform(get("/authors/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("ADMIN")
    @Test
    public void find_invalidBookId_404() throws Exception {
        mockMvc.perform(get("/authors/4"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
