package com.example.demoCode.repository;

import com.example.demoCode.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Records, Long> {

    @Query(value = "SELECT * FROM records ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Records> findRandomRecords();
}
