package com.example.demoCode.repository;

import com.example.demoCode.entity.Contacts;
import com.example.demoCode.entity.GPSInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GPSInfoRepository extends JpaRepository<GPSInfo, Long> {

    @Query(value = "SELECT * FROM gpsinfo ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<GPSInfo> findRandomGpsInfo();
}
