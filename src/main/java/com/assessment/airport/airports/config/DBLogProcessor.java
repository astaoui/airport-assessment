package com.assessment.airport.airports.config;

import com.assessment.airport.airports.domaine.Country;

import org.springframework.batch.item.ItemProcessor;

public class DBLogProcessor implements ItemProcessor<Country, Country> {
    public Country process(Country country) throws Exception {
        // System.out.println("Inserting country : " + country);
        return country;
    }
}