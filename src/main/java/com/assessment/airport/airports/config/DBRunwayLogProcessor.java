package com.assessment.airport.airports.config;

import com.assessment.airport.airports.domaine.Airport;
import com.assessment.airport.airports.domaine.Runway;
import com.assessment.airport.airports.repository.AirportRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class DBRunwayLogProcessor implements ItemProcessor<Runway, Runway> {
    @Autowired
    AirportRepository airportRepository;

    public Runway process(Runway runway) throws Exception {
        /*
         * Airport airport = airportRepository.getByident(runway.getAirport_ident());
         * runway.setAirport(airport);
         */
        // System.out.println("Inserting runway : " + runway);
        return runway;
    }
}