package com.ceng453.gameServer.repository;

import com.ceng453.gameServer.dao.RecordDAO;
import com.ceng453.gameServer.model.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query( "SELECT record.user.username, record.score, record.date " +
            "FROM Record record " +
            "ORDER BY record.score DESC ")
    List<Object []> findAllRecords(Pageable pageable);

    @Query( "SELECT record.user.username, record.score, record.date " +
            "FROM Record record " +
            "WHERE record.date <= :date " +
            "ORDER BY record.score DESC ")
    List<Object []> findAllRecordsBefore(@Param("date") Long date, Pageable pageable);

}
