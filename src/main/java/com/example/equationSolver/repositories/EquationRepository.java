package com.example.equationSolver.repositories;

import com.example.equationSolver.entities.EquationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquationRepository extends JpaRepository<EquationEntity, Long> {
}
