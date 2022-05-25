package com.piotrdulewski.foundationapp.repository;

import com.piotrdulewski.foundationapp.entity.RequestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCategoryRepository extends JpaRepository<RequestCategory, Long> {
}