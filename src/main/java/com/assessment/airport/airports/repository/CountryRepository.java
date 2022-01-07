package com.assessment.airport.airports.repository;

import java.util.List;

import com.assessment.airport.airports.domaine.AiroportCount;
import com.assessment.airport.airports.domaine.Country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    public List<Country> findByNameStartsWith(String name);

    @Query(value = "SELECT r.id FROM airport a , runway r  where a.iso_country=(:code) and a.ident= r.airport_ident", nativeQuery = true)
    public List<Long> getRunwaysByCountryCode(@Param("code") String code);

    @Query(value = "SELECT  c.name, COUNT(c.name) as count FROM country c, airport a WHERE c.code=a.iso_country GROUP BY c.name ORDER BY COUNT(c.name) DESC limit 10", nativeQuery = true)
    public List<AiroportCount> topTenCountriesByAirportNumber();

    public Country getByCode(String iso_country);

}
