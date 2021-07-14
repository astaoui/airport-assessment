package com.assessment.airport.airports.config;

import com.assessment.airport.airports.domaine.Airport;
import com.assessment.airport.airports.domaine.Country;
import com.assessment.airport.airports.repository.CountryRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class DBAirportLogProcessor implements ItemProcessor<Airport, Airport> {

    @Autowired
    CountryRepository countryRepository;

    public Airport process(Airport airport) throws Exception {
        /*
         * Country country = countryRepository.getByCode(airport.getIso_country());
         * airport.setCountry(country);
         */
        // System.out.println("Inserting Airport : " + airport);
        return airport;
    }
}