package com.ceng453.gameServer.services;

import com.ceng453.gameServer.model.Record;
import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.RecordRepository;
import com.ceng453.gameServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public void addRecord(Long userID, Long score) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            Record record = new Record();
            record.setUser(user.get());
            record.setScore(score);
            record.setDate(System.currentTimeMillis());
            recordRepository.save(record);
        }
    }

    public List<Record> getAllRecords(int pageLimit) {

        return recordRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
    }

    public List<Record> getMonthlyRecords(int pageLimit) {

        Long oneMonth = System.currentTimeMillis() - (long) (43200000); // (30 * 24 * 60 * 1000);
        return recordRepository.findAllByDateGreaterThanEqualOrderByScoreDesc(oneMonth);
    }

    public List<Record> getWeeklyRecords(int pageLimit) {

        Long oneWeek = System.currentTimeMillis() - (long) (10080000); //(7 * 24 * 60 * 1000);
        return recordRepository.findAllByDateGreaterThanEqualOrderByScoreDesc(oneWeek);
    }

}
