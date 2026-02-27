package com.educationalSystem.repository;

import com.educationalSystem.entity.user.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
}
