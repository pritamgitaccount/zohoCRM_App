package com.zohocrm.repository;

import com.zohocrm.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, String> {

    Optional<Lead>findByEmail(String email);

    Optional<Lead> findByMobile(long mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(long mobile);
}