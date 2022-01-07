package com.assessment.airport.airports.repository;

import com.assessment.airport.airports.domaine.Airport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Airport getByident(String airport_ident);
}
