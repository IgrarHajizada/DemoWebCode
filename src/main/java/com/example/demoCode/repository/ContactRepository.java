package com.example.demoCode.repository;

import com.example.demoCode.entity.Contacts;
import com.example.demoCode.entity.DataInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, Long> {

    @Query(value = "SELECT * FROM contacts ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Contacts> findRandomContacts();

//
}
