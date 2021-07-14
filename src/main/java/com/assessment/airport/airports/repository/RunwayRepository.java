package com.assessment.airport.airports.repository;

import com.assessment.airport.airports.domaine.Runway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunwayRepository extends JpaRepository<Runway, Long> {
}