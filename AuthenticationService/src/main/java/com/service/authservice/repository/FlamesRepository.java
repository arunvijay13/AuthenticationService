package com.service.authservice.repository;

import com.service.authservice.entity.Flames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlamesRepository extends JpaRepository<Flames, Long> {
}
