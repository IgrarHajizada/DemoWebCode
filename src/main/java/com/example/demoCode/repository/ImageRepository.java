package com.example.demoCode.repository;

import com.example.demoCode.entity.Contacts;
import com.example.demoCode.entity.DataInfo;
import com.example.demoCode.entity.GPSInfo;
import com.example.demoCode.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Images, Long> {

    @Query(value = "SELECT * FROM images ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Images> findRandomImages();


}
