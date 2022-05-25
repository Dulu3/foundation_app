package com.piotrdulewski.foundationapp.repository;

import com.piotrdulewski.foundationapp.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}