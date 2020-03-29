package com.ceng453.gameServer.services;

import com.ceng453.gameServer.dao.RecordDAO;
import com.ceng453.gameServer.model.Record;
import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.RecordRepository;
import com.ceng453.gameServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<RecordDAO> getAllRecords(int pageLimit) {

        return makeRecordDAOList(recordRepository.findAllRecords(PageRequest.of(0, pageLimit)));
    }

    public List<RecordDAO> getMonthlyRecords(int pageLimit) {

        Long oneMonth = System.currentTimeMillis() - (long) (43200000); // (30 * 24 * 60 * 1000);

        return makeRecordDAOList(recordRepository.findAllRecordsBefore(oneMonth, PageRequest.of(0, pageLimit)));
    }

    public List<RecordDAO> getWeeklyRecords(int pageLimit) {

        Long oneWeek = System.currentTimeMillis() - (long) (10080000); //(7 * 24 * 60 * 1000);

        return makeRecordDAOList(recordRepository.findAllRecordsBefore(oneWeek, PageRequest.of(0, pageLimit)));
    }

    public List<RecordDAO> makeRecordDAOList(List<Object[]> queryResultList) {

        List<RecordDAO> recordDAOList = new ArrayList<>();

        for (Object[] record : queryResultList) {
            recordDAOList.add(new RecordDAO((String) record[0], (long) record[1], (long) record[2]));
        }

        return recordDAOList;
    }

}
