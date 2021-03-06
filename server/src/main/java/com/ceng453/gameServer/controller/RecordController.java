package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.dao.RecordDAO;
import com.ceng453.gameServer.services.RecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecordController {

    /**
     * Record object keeps date as long type milliseconds not as Date object.
     * Before displayed to the user below formatters can be used.
     * <p>
     * long time = System.currentTimeMillis();
     * SimpleDateFormat formatter1= new SimpleDateFormat("HH:mm:ss 'at' dd-MM-yyyy");
     * SimpleDateFormat formatter2= new SimpleDateFormat("HH:mm:ss 'at' dd MMMM yyyy");
     * System.out.println(formatter1.format(time)); // 14:40:29 at 28-03-2020
     * System.out.println(formatter2.format(time)); // 14:40:29 at 28 March 2020
     */
    private final RecordService recordService;

    /**
     * This method maps POST Request to /record and saves record with given ID of user and score.
     *
     * @param userID ID of the user
     * @param score  Score of the user
     * @throws Exception if user does not exist or deleted
     */
    @PostMapping("/record")
    @ApiOperation(value = "Saves record with given ID of user and score",
            notes = "Provide ID of user and score of user to save the record",
            response = void.class)
    public void addRecord(@ApiParam(value = "ID of the user")
                          @RequestParam(value = "userID") Long userID,
                          @ApiParam(value = "Score of the user")
                          @RequestParam(value = "score") Long score) throws Exception {
        recordService.addRecord(userID, score);
    }

    /**
     * This method maps GET Request to /leaderboard_all
     *
     * @param pageLimit Size of the returning list. Should be positive int
     * @return A list of records ordered by Score
     */
    @GetMapping("/leaderboard_all")
    @ApiOperation(value = "Gets highest N(pageLimit) all scores from the database",
            notes = "Provide page limit for receiving that number of elements in returning list",
            response = RecordDAO.class,
            responseContainer = "List")
    public List<RecordDAO> getAllRecords(@ApiParam(value = "Page limit for receiving that number of elements in returning list. Should be positive int")
                                         @RequestParam(value = "pageLimit") int pageLimit) {
        return recordService.getAllRecords(pageLimit);
    }

    /**
     * This method maps GET Request to /leaderboard_monthly
     *
     * @param pageLimit Size of the returning list. Should be positive int
     * @return A list of last month's records ordered by Score
     */
    @GetMapping("/leaderboard_monthly")
    @ApiOperation(value = "Gets highest N(pageLimit) scores of last month from the database",
            notes = "Provide page limit for receiving that number of elements in returning list",
            response = RecordDAO.class,
            responseContainer = "List")
    public List<RecordDAO> getMonthlyRecords(@ApiParam(value = "Page limit for receiving that number of elements in returning list. Should be positive int")
                                             @RequestParam(value = "pageLimit") int pageLimit) {
        return recordService.getMonthlyRecords(pageLimit);
    }

    /**
     * This method maps GET Request to /leaderboard_weekly
     *
     * @param pageLimit Size of the returning list. Should be positive int
     * @return A list of last week's records ordered by Score
     */
    @GetMapping("/leaderboard_weekly")
    @ApiOperation(value = "Gets highest N(pageLimit) scores of last week from the database",
            notes = "Provide page limit for receiving that number of elements in returning list",
            response = RecordDAO.class,
            responseContainer = "List")
    public List<RecordDAO> getWeeklyRecords(@ApiParam(value = "Page limit for receiving that number of elements in returning list. Should be positive int")
                                            @RequestParam(value = "pageLimit") int pageLimit) {
        return recordService.getWeeklyRecords(pageLimit);
    }


}
