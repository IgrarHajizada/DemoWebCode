package com.example.demoCode.repository;

import com.example.demoCode.entity.Contacts;
import com.example.demoCode.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, Long> {

}