package com.raczkowski.springintro;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        MvcResult mvcResult = mockMvc.perform(get("/users/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Hello World"))
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void should_return_all_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Przemek Raczkowski"))
                .andExpect(jsonPath("$.[0].address").value("Kraków, ul. Kijowska 54"))
                .andExpect(jsonPath("$.[0].phoneNumber").value("733897222"))
                .andExpect(jsonPath("$.[1].name").value("Tomasz Hajto"))
                .andExpect(jsonPath("$.[1].address").value("Berlin, Herta StraBe"))
                .andExpect(jsonPath("$.[1].phoneNumber").value("67782323"));
    }
}
