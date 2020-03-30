package com.ceng453.gameServer;

import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.After;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GameServerApplicationTests {

    private MockMvc mockMvc;
    private String jwt;

    @Autowired
    private UserRepository userRepository;

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

    @Test
    public void Success_UserController_GetUserId_Test() throws Exception {
        Long id = userRepository.findByUsername("testPurpose").get().getId();

        mockMvc.perform(
                get("/api/getUserID")
                        .queryParam("username","testPurpose")
                )
                .andExpect(MockMvcResultMatchers.content().string( Long.toString(id) ));
    }

    @Test(expected = Exception.class)
    public void DeletedUserNotFound_UserController_GetUserId_Test() throws Exception {

        mockMvc.perform(
                get("/api/getUserID")
                        .queryParam("username","deletedUserTest")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());;
    }

    @Test(expected = Exception.class)
    public void NoSuchUserFound_UserController_GetUserId_Test() throws Exception {

        mockMvc.perform(
                get("/api/getUserID")
                        .queryParam("username","hopeNotInDB")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());;
    }

    @Test
    public void Success_UserController_GetAllUsers_Test() throws Exception {

        List<User> userList = new ArrayList<>(userRepository.findAll());

        mockMvc.perform( get("/api/users") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(userList.size())));
    }

    @Test
    public void Success_UserController_GetUser_Test() throws Exception {
        User user = userRepository.findById(2L).get();

        mockMvc.perform(
                get("/api/profile")
                        .queryParam("id","2")
        )
                .andExpect(MockMvcResultMatchers.content().string(user.toString()));
    }

    @Test(expected = Exception.class)
    public void DeletedUserNotFound_UserController_GetUser_Test() throws Exception {

        mockMvc.perform(
                get("/api/profile")
                        .queryParam("id","6")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());;
    }

    @Test(expected = Exception.class)
    public void NoSuchUserFound_UserController_GetUser_Test() throws Exception {

        mockMvc.perform(
                get("/api/profile")
                        .queryParam("id","-1")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());;
    }

    @Test
    public void Success_UserController_UpdateUser_Test() throws Exception {
        String id = Long.toString(userRepository.findByUsername("testPurpose").get().getId());

        mockMvc.perform(
                put("/api/profile")
                        .queryParam("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is updated successfully."));
    }

    @Test
    public void DeletedUserNotUpdated_UserController_UpdateUser_Test() throws Exception {
        String id = Long.toString(userRepository.findByUsername("deletedUserTest").get().getId());

        mockMvc.perform(
                put("/api/profile")
                        .queryParam("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"deletedUserTest\",\"password\":\"test\" } ")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the id."));
    }

    @Test
    public void NoSuchUserFound_UserController_UpdateUser_Test() throws Exception {
        mockMvc.perform(
                put("/api/profile")
                        .queryParam("id", "-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"deletedUserTest\",\"password\":\"test\" } ")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the input fields and id."));
    }

    @Test
    public void Success_UserController_DeleteUser_Test() throws Exception {

        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"toBeDeleted\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created successfully. Please log in"));

        String id = Long.toString(userRepository.findByUsername("toBeDeleted").get().getId());

        mockMvc.perform(
                delete("/api/profile")
                        .queryParam("id", id)

        )
                .andExpect(MockMvcResultMatchers.content().string("User is deleted successfully."));

        userRepository.delete(userRepository.findByUsername("toBeDeleted").get());
    }

    @Test
    public void DeletedUserNotUpdated_UserController_DeleteUser_Test() throws Exception {
        String id = Long.toString(userRepository.findByUsername("deletedUserTest").get().getId());

        mockMvc.perform(
                delete("/api/profile")
                        .queryParam("id", id)

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the id."));
    }

    @Test
    public void NoSuchUserFound_UserController_DeleteUser_Test() throws Exception {
        mockMvc.perform(
                delete("/api/profile")
                        .queryParam("id", "-1")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the id."));
    }


}
