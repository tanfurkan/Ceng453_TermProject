package com.ceng453.gameServer;

import com.ceng453.gameServer.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GameServerApplicationTests {

    private MockMvc mockMvc;
    private String jwt;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void Success_UserController_Register_Test() throws Exception {

        userRepository.delete(userRepository.findByUsername("testPurpose").get());

        mockMvc.perform(
                    post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created successfully. Please log in"));

    }

    @Test
    public void AlreadyTaken_UserController_Register_Test() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Username is already taken."));

    }

    @Test
    public void EmptyUsername_UserController_Register_Test() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Username cannot be empty."));
    }

    @Test
    public void EmptyPassword_UserController_Register_Test() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password cannot be empty."));
    }

    @Test
    public void Success_UserController_Login_Test() throws Exception {
        MvcResult result  = mockMvc.perform(
                        post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
                        )
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        this.jwt = (String) JsonPath.parse(result.getResponse().getContentAsString()).read("$.jwt");
    }

    @Test(expected = Exception.class)
    public void WrongCredential_UserController_Login_Test() throws Exception {
        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"hocamsizicokseviyoruz\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test(expected = Exception.class)
    public void EmptyCredential_UserController_Login_Test() throws Exception {
        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"hocamsizicokseviyoruz\",\"password\":\"\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    
}
