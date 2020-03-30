package com.ceng453.gameServer;

import com.ceng453.gameServer.dao.RecordDAO;
import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.RecordRepository;
import com.ceng453.gameServer.repository.UserRepository;
import com.ceng453.gameServer.services.RecordService;
import com.jayway.jsonpath.JsonPath;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

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
    private RecordRepository recordRepository;

    @Autowired
    private RecordService recordService;

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

    @Test
    public void Success_RecordController_AddRecord_Test() throws Exception {

        mockMvc.perform(
                post("/api/record")
                        .param("userID", "3")
                        .param("score", "333")
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test(expected = Exception.class)
    public void NotFoundUser_RecordController_AddRecord_Test() throws Exception {
        mockMvc.perform(
                post("/api/record")
                        .param("userID", "-1")
                        .param("score", "333")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());

    }

    @Test(expected = Exception.class)
    public void DeletedUser_RecordController_AddRecord_Test() throws Exception {
        mockMvc.perform(
                post("/api/record")
                        .param("userID", "6")
                        .param("score", "333")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test(expected = Exception.class)
    public void NegativeScore_RecordController_AddRecord_Test() throws Exception {
        mockMvc.perform(
                post("/api/record")
                        .param("userID", "3")
                        .param("score", "-1")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    public void AllRecords_RecordController_GetAllRecords_Test() throws Exception {

        List<Object []> recordList = recordRepository.findAllRecords(PageRequest.of(0, 20));

        mockMvc.perform( get("/api/leaderboard_all").param("pageLimit","20") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));

        recordList = recordRepository.findAllRecords(PageRequest.of(0, 5));

        mockMvc.perform( get("/api/leaderboard_all").param("pageLimit","5") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));
    }

    @Test(expected = Exception.class)
    public void ErrorAllRecords_RecordController_GetAllRecords_Test() throws Exception {

        mockMvc.perform( get("/api/leaderboard_all").param("pageLimit","-2") )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void MonthlyRecords_RecordController_GetAllRecords_Test() throws Exception {

        Long oneMonth = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

        List<Object []> recordList = recordRepository.findAllRecordsAfter(oneMonth, PageRequest.of(0, 20));

        mockMvc.perform( get("/api/leaderboard_monthly").param("pageLimit","20") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));

        recordList = recordRepository.findAllRecordsAfter(oneMonth, PageRequest.of(0, 5));

        mockMvc.perform( get("/api/leaderboard_monthly").param("pageLimit","5") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));
    }

    @Test(expected = Exception.class)
    public void ErrorMonthlyRecords_RecordController_GetAllRecords_Test() throws Exception {

        mockMvc.perform( get("/api/leaderboard_monthly").param("pageLimit","-2") )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void WeeklyRecords_RecordController_GetAllRecords_Test() throws Exception {

        Long oneWeek = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);

        List<Object []> recordList = recordRepository.findAllRecordsAfter(oneWeek, PageRequest.of(0, 20));

        mockMvc.perform( get("/api/leaderboard_weekly").param("pageLimit","20") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));

        recordList = recordRepository.findAllRecordsAfter(oneWeek, PageRequest.of(0, 5));

        mockMvc.perform( get("/api/leaderboard_weekly").param("pageLimit","5") )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));
    }

    @Test(expected = Exception.class)
    public void ErrorWeeklyRecords_RecordController_GetAllRecords_Test() throws Exception {

        mockMvc.perform( get("/api/leaderboard_weekly").param("pageLimit","-2") )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void is_getAllRecords_Successfully() throws Exception{
        List<RecordDAO> listBeforeAdd = recordService.getAllRecords(Integer.MAX_VALUE-1);
        recordService.addRecord(3L,333L);
        List<RecordDAO> listAfterAdd = recordService.getAllRecords(Integer.MAX_VALUE-1);
        assertEquals(listAfterAdd.size(),listBeforeAdd.size()+1);
    }

}
