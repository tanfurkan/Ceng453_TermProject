package com.ceng453.gameServer.repository;

import com.ceng453.gameServer.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findAllByDateGreaterThanEqualOrderByScoreDesc(Long date);

}
