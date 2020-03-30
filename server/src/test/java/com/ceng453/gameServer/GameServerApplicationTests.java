package com.ceng453.gameServer;

import com.ceng453.gameServer.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GameServerApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void SuccessUserControllerRegisterTest() throws Exception {
        mockMvc.perform(
                    post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created successfully. Please log in"));

    }

    @Test
    public void AlreadyTakenUserControllerRegisterTest() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Username is already taken."));

        userRepository.delete(userRepository.findByUsername("testPurpose").get());
    }

    @Test
    public void EmptyUsernameUserControllerRegisterTest() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Username cannot be empty."));
    }

    @Test
    public void EmptyPasswordUserControllerRegisterTest() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password cannot be empty."));
    }

}
