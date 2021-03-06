package com.ceng453.gameServer.repository;

import com.ceng453.gameServer.model.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    /**
     * This method creates a query to get all records
     *
     * @param pageable This parameter is used for paging and limiting the query result.
     * @return A list of records sorted highest to lowest
     */
    @Query("SELECT record.user.username, record.score, record.date " +
            "FROM Record record " +
            "ORDER BY record.score DESC, record.date DESC")
    List<Object[]> findAllRecords(Pageable pageable);

    /**
     * This method creates a query to get all records that are after the given date
     *
     * @param pageable This parameter is used for paging and limiting the query result.
     * @return A list of records that are after the given date sorted highest to lowest
     */
    @Query("SELECT record.user.username, record.score, record.date " +
            "FROM Record record " +
            "WHERE record.date >= :date " +
            "ORDER BY record.score DESC, record.date DESC")
    List<Object[]> findAllRecordsAfter(@Param("date") Long date, Pageable pageable);

}
