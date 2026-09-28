package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByCode(String code);
    List<Contact> findByType(String type);
}
