package com.raczkowski.springintro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raczkowski.springintro.dto.UserDto;
import com.raczkowski.springintro.exception.UserNotFoundException;
import com.raczkowski.springintro.model.User;
import com.raczkowski.springintro.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void should_return_hello_world() throws Exception {
        mockMvc.perform(get("/users/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").value("Hello World"))
                .andReturn();
    }

    @Test
    public void should_return_all_users() throws Exception {
        when(userService.getAllUsers()).thenReturn(buildDummyUsers());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Przemek Raczkowski"))
                .andExpect(jsonPath("$.[0].address").value("Kraków, ul. Kijowska 54"))
                .andExpect(jsonPath("$.[0].phoneNumber").value("733897222"))
                .andExpect(jsonPath("$.[1].name").value("Tomasz Hajto"))
                .andExpect(jsonPath("$.[1].address").value("Berlin, Herta StraBe"))
                .andExpect(jsonPath("$.[1].phoneNumber").value("67782323"));
    }

    @Test
    public void should_return_user_for_given_id() throws Exception {
        int userId = 1;

        when(userService.getUser(userId)).thenReturn(buildDummyUsers().get(0));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Przemek Raczkowski"))
                .andExpect(jsonPath("$.address").value("Kraków, ul. Kijowska 54"))
                .andExpect(jsonPath("$.phoneNumber").value("733897222"));
    }

    @Test
    public void should_sort_users_by_request_param() throws Exception {
        when(userService.getAllUsers()).thenReturn(buildDummyUsers());

        mockMvc.perform(get("/users").param("order", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Przemek Raczkowski"))
                .andExpect(jsonPath("$.[0].address").value("Kraków, ul. Kijowska 54"))
                .andExpect(jsonPath("$.[0].phoneNumber").value("733897222"))
                .andExpect(jsonPath("$.[1].name").value("Tomasz Hajto"))
                .andExpect(jsonPath("$.[1].address").value("Berlin, Herta StraBe"))
                .andExpect(jsonPath("$.[1].phoneNumber").value("67782323"));

        mockMvc.perform(get("/users").param("order", "DESC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Tomasz Hajto"))
                .andExpect(jsonPath("$.[0].address").value("Berlin, Herta StraBe"))
                .andExpect(jsonPath("$.[0].phoneNumber").value("67782323"))
                .andExpect(jsonPath("$.[1].name").value("Przemek Raczkowski"))
                .andExpect(jsonPath("$.[1].address").value("Kraków, ul. Kijowska 54"))
                .andExpect(jsonPath("$.[1].phoneNumber").value("733897222"));
    }

    @Test
    public void should_return_created_status_code_when_adding_new_user() throws Exception {
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(convertUserDtoToJson(new UserDto("Andrew Golara", "Chicago", "+557263723"))))
                .andExpect(status().isCreated());

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void should_return_not_found_status_code_when_user_does_not_exist_for_given_id() throws Exception {
        int userId = 100;

        when(userService.getUser(userId)).thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User not found with given id: 100"));
    }

    private String convertUserDtoToJson(UserDto userDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(userDto);
    }

    private List<User> buildDummyUsers() {
        return asList(
                new User("Przemek Raczkowski",
                        "Kraków, ul. Kijowska 54",
                        "733897222"),
                new User("Tomasz Hajto",
                        "Berlin, Herta StraBe",
                        "67782323")
        );
    }
}
