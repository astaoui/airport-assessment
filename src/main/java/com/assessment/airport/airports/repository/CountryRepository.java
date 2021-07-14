package com.assessment.airport.airports.repository;

import java.util.List;
import java.util.Map;

import com.assessment.airport.airports.domaine.AiroportCount;
import com.assessment.airport.airports.domaine.Country;
import com.assessment.airport.airports.domaine.Runway;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    public List<Country> findByNameStartsWith(String name);

    @Query(value = "SELECT R.id FROM AIRPORT A , RUNWAY R  where a.ISO_COUNTRY=(:code) and a.IDENT= r.AIRPORT_IDENT", nativeQuery = true)
    public List<Long> getRunwaysByCountryCode(@Param("code") String code);

    @Query(value = "SELECT c.NAME, COUNT(c.code)s FROM COUNTRY c, AIRPORT a WHERE c.CODE=a.ISO_COUNTRY GROUP BY c.CODE ORDER BY COUNT(c.code) DESC limit 10", nativeQuery = true)
    public List<Object[]> topTenCountriesByAirportNumber();

    public Country getByCode(String iso_country);

}
