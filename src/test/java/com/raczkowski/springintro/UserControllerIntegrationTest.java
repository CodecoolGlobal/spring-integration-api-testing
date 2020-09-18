package com.raczkowski.springintro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raczkowski.springintro.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

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
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Przemek Raczkowski"))
                .andExpect(jsonPath("$.address").value("Kraków, ul. Kijowska 54"))
                .andExpect(jsonPath("$.phoneNumber").value("733897222"));
    }

    @Test
    public void should_sort_users_by_request_param() throws Exception {
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

//    @Test
//    public void should_return_created_status_code_when_adding_new_user() throws Exception {
//        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(convertUserDtoToJson(new UserDto("Andrew Golara", "Chicago", "+557263723"))))
//                .andExpect(status().isCreated());
//    }

    @Test
    public void should_return_not_found_status_code_when_user_does_not_exist_for_given_id() throws Exception {
        mockMvc.perform(get("/users/{id}", 100))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User not found with given id: 100"));
    }

    private String convertUserDtoToJson(UserDto userDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(userDto);
    }
}
