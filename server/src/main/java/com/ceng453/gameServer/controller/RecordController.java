package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.model.Record;
import com.ceng453.gameServer.services.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    /*
        Record object keeps date as long type milliseconds not as Date object.
        Before displayed to the user below formatters can be used.

        long time = System.currentTimeMillis();
		SimpleDateFormat formatter1= new SimpleDateFormat("HH:mm:ss 'at' dd-MM-yyyy");
		SimpleDateFormat formatter2= new SimpleDateFormat("HH:mm:ss 'at' dd MMMM yyyy");
		System.out.println(formatter1.format(time)); // 14:40:29 at 28-03-2020
		System.out.println(formatter2.format(time)); // 14:40:29 at 28 March 2020
    */

    //
    @PostMapping("/record")
    public void addRecord(@RequestParam(value = "userID") Long userID,
                          @RequestParam(value = "score") Long score){
        recordService.addRecord(userID, score);
    }

    // GET Request to /leaderboard_all return a list of records ordered by Score
    @GetMapping("/leaderboard_all")
    public List<Record> getAllRecords(){
        return recordService.getAllRecords();
    }

    // GET Request to /leaderboard_monthly return a list of last month's records ordered by Score
    @GetMapping("/leaderboard_monthly")
    public List<Record> getMonthlyRecords(){
        return recordService.getMonthlyRecords();
    }

    // GET Request to /leaderboard_weekly return a list of last week's records ordered by Score
    @GetMapping("/leaderboard_weekly")
    public List<Record> getWeeklyRecords(){
        return recordService.getWeeklyRecords();
    }


}
