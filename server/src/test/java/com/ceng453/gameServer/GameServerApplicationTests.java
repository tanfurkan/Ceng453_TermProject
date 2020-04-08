package com.ceng453.gameServer;

import com.ceng453.gameServer.dao.RecordDAO;
import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.RecordRepository;
import com.ceng453.gameServer.repository.UserRepository;
import com.ceng453.gameServer.services.RecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GameServerApplicationTests {

    private MockMvc mockMvc;

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
    public void success_user_controller_register_test() throws Exception {

        Optional<User> optionalUser = userRepository.findByUsername("testPurpose");
        optionalUser.ifPresent(user -> userRepository.delete(user));

        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created successfully. Please log in"));

    }

    @Test
    public void already_taken_user_controller_register_test() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Username is already taken."));

    }

    @Test
    public void empty_username_user_controller_register_test() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Username cannot be empty."));
    }

    @Test
    public void empty_password_user_controller_register_test() throws Exception {
        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password cannot be empty."));
    }

    @Test
    public void success_user_controller_login_test() throws Exception {
        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test(expected = Exception.class)
    public void wrong_credential_user_controller_login_test() throws Exception {
        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"hocamsizicokseviyoruz\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test(expected = Exception.class)
    public void empty_credential_user_controller_login_test() throws Exception {
        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"hocamsizicokseviyoruz\",\"password\":\"\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    public void success_user_controller_get_user_id_test() throws Exception {

        Long id = -1L;
        Optional<User> optionalUser = userRepository.findByUsername("testPurpose");
        if(optionalUser.isPresent()) id = optionalUser.get().getId();

        mockMvc.perform(
                get("/api/getUserID")
                        .queryParam("username", "testPurpose")
        )
                .andExpect(MockMvcResultMatchers.content().string(Long.toString(id)));
    }

    @Test(expected = Exception.class)
    public void deleted_user_not_found_user_controller_get_user_id_test() throws Exception {

        mockMvc.perform(
                get("/api/getUserID")
                        .queryParam("username", "deletedUserTest")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test(expected = Exception.class)
    public void no_such_user_found_user_controller_get_user_id_test() throws Exception {

        mockMvc.perform(
                get("/api/getUserID")
                        .queryParam("username", "hopeNotInDB")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    public void success_user_controller_get_all_users_test() throws Exception {

        List<User> userList = new ArrayList<>(userRepository.findAll());

        mockMvc.perform(get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(userList.size())));
    }

    @Test
    public void success_user_controller_get_user_test() throws Exception {
        User user = null;
        Optional<User> optionalUser = userRepository.findById(2L);
        if(optionalUser.isPresent()) user = optionalUser.get();

        assert user != null;
        mockMvc.perform(
                get("/api/profile")
                        .queryParam("id", "2")
        )
                .andExpect(MockMvcResultMatchers.content().string(user.toString()));
    }

    @Test(expected = Exception.class)
    public void deleted_user_not_found_user_controller_get_user_test() throws Exception {

        mockMvc.perform(
                get("/api/profile")
                        .queryParam("id", "6")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test(expected = Exception.class)
    public void no_such_user_found_user_controller_get_user_test() throws Exception {

        mockMvc.perform(
                get("/api/profile")
                        .queryParam("id", "-1")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    public void success_user_controller_update_user_test() throws Exception {

        String id = null;
        Optional<User> optionalUser = userRepository.findByUsername("testPurpose");

        if(optionalUser.isPresent()) id = Long.toString(optionalUser.get().getId());

        assert id != null;
        mockMvc.perform(
                put("/api/profile")
                        .queryParam("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"testPurpose\",\"password\":\"123\" } ")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is updated successfully."));
    }

    @Test
    public void deleted_user_not_updated_user_controller_update_user_test() throws Exception {

        String id = null;
        Optional<User> optionalUser = userRepository.findByUsername("deletedUserTest");

        if(optionalUser.isPresent()) id = Long.toString(optionalUser.get().getId());

        assert id != null;
        mockMvc.perform(
                put("/api/profile")
                        .queryParam("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"deletedUserTest\",\"password\":\"test\" } ")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the id."));
    }

    @Test
    public void no_such_user_found_user_controller_update_user_test() throws Exception {
        mockMvc.perform(
                put("/api/profile")
                        .queryParam("id", "-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"deletedUserTest\",\"password\":\"test\" } ")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the input fields and id."));
    }

    @Test
    public void success_user_controller_delete_user_test() throws Exception {

        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" { \"username\":\"toBeDeleted\",\"password\":\"123\" } ")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created successfully. Please log in"));

        String id = null;
        Optional<User> optionalUser = userRepository.findByUsername("toBeDeleted");

        if(optionalUser.isPresent()) id = Long.toString(optionalUser.get().getId());

        assert id != null;

        mockMvc.perform(
                delete("/api/profile")
                        .queryParam("id", id)

        )
                .andExpect(MockMvcResultMatchers.content().string("User is deleted successfully."));

        Optional<User> optionalUser2 = userRepository.findByUsername("toBeDeleted");
        optionalUser2.ifPresent(user -> userRepository.delete(user));
    }

    @Test
    public void deleted_user_not_updated_user_controller_delete_user_test() throws Exception {

        String id = null;
        Optional<User> optionalUser = userRepository.findByUsername("deletedUserTest");

        if(optionalUser.isPresent()) id = Long.toString(optionalUser.get().getId());

        assert id != null;
        mockMvc.perform(
                delete("/api/profile")
                        .queryParam("id", id)

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the id."));
    }

    @Test
    public void no_such_user_found_user_controller_delete_user_test() throws Exception {
        mockMvc.perform(
                delete("/api/profile")
                        .queryParam("id", "-1")

        )
                .andExpect(MockMvcResultMatchers.content().string("User is not found, please check the id."));
    }

    @Test
    public void success_record_controller_add_record_test() throws Exception {

        mockMvc.perform(
                post("/api/record")
                        .param("userID", "3")
                        .param("score", "333")
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test(expected = Exception.class)
    public void not_found_user_record_controller_add_record_test() throws Exception {
        mockMvc.perform(
                post("/api/record")
                        .param("userID", "-1")
                        .param("score", "333")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());

    }

    @Test(expected = Exception.class)
    public void deleted_user_record_controller_add_record_test() throws Exception {
        mockMvc.perform(
                post("/api/record")
                        .param("userID", "6")
                        .param("score", "333")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test(expected = Exception.class)
    public void negative_score_record_controller_add_record_test() throws Exception {
        mockMvc.perform(
                post("/api/record")
                        .param("userID", "3")
                        .param("score", "-1")
        )
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    public void all_records_record_controller_get_all_records_test() throws Exception {

        List<Object[]> recordList = recordRepository.findAllRecords(PageRequest.of(0, 20));

        mockMvc.perform(get("/api/leaderboard_all").param("pageLimit", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));

        recordList = recordRepository.findAllRecords(PageRequest.of(0, 5));

        mockMvc.perform(get("/api/leaderboard_all").param("pageLimit", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));
    }

    @Test(expected = Exception.class)
    public void error_all_records_record_controller_get_all_records_test() throws Exception {

        mockMvc.perform(get("/api/leaderboard_all").param("pageLimit", "-2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void monthly_records_record_controller_get_all_records_test() throws Exception {

        Long oneMonth = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

        List<Object[]> recordList = recordRepository.findAllRecordsAfter(oneMonth, PageRequest.of(0, 20));

        mockMvc.perform(get("/api/leaderboard_monthly").param("pageLimit", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));

        recordList = recordRepository.findAllRecordsAfter(oneMonth, PageRequest.of(0, 5));

        mockMvc.perform(get("/api/leaderboard_monthly").param("pageLimit", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));
    }

    @Test(expected = Exception.class)
    public void error_monthly_records_record_controller_get_all_records_test() throws Exception {

        mockMvc.perform(get("/api/leaderboard_monthly").param("pageLimit", "-2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void weekly_records_record_controller_get_all_records_test() throws Exception {

        Long oneWeek = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);

        List<Object[]> recordList = recordRepository.findAllRecordsAfter(oneWeek, PageRequest.of(0, 20));

        mockMvc.perform(get("/api/leaderboard_weekly").param("pageLimit", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));

        recordList = recordRepository.findAllRecordsAfter(oneWeek, PageRequest.of(0, 5));

        mockMvc.perform(get("/api/leaderboard_weekly").param("pageLimit", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(recordList.size())));
    }

    @Test(expected = Exception.class)
    public void error_weekly_records_record_controller_get_all_records_test() throws Exception {

        mockMvc.perform(get("/api/leaderboard_weekly").param("pageLimit", "-2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void is_get_all_records_successfully_test() throws Exception {
        List<RecordDAO> listBeforeAdd = recordService.getAllRecords(Integer.MAX_VALUE - 1);
        recordService.addRecord(3L, 333L);
        List<RecordDAO> listAfterAdd = recordService.getAllRecords(Integer.MAX_VALUE - 1);
        assertEquals(listAfterAdd.size(), listBeforeAdd.size() + 1);
    }

}
