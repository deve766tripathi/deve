package com.examle.deve;

import com.examle.deve.model.User;
import com.examle.deve.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private List<User> manyUsers;
    private long initialUserCount;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        objectMapper.registerModules(new JavaTimeModule());

        User initialUser1 = new User("John Doe", "john@example.com");
        User initialUser2 = new User("Jane Smith", "jane@example.com");
        List<User> savedInitialUsers = userRepository.saveAll(Arrays.asList(initialUser1, initialUser2));

        this.user1 = savedInitialUsers.get(0);
        this.user2 = savedInitialUsers.get(1);

        manyUsers = IntStream.rangeClosed(1, 25)
                .mapToObj(i -> new User("User" + i, "users" + i + "@example.com"))
                .collect(Collectors.toList());
        userRepository.saveAll(manyUsers);

        initialUserCount = userRepository.count();
    }

    @Test
    public void testGetUserByIdFound() throws Exception {
        mockMvc.perform(get("/users/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUser() throws Exception {
        User newUser1 = new User("Alice Brown", "alice.brown@example.com");
        User newUser2 = new User("Chris Gayle", "chris.gayle@example.com");
        List<User> newUsers = Arrays.asList(newUser1, newUser2);

        mockMvc.perform(post("/users/Multibody")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUsers)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", is("Alice Brown")));
    }
}
