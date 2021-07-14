package com.assessment.airport.airports.controller;

import java.util.ArrayList;
import java.util.List;

import com.assessment.airport.airports.domaine.AiroportCount;
import com.assessment.airport.airports.domaine.Country;
import com.assessment.airport.airports.domaine.Runway;
import com.assessment.airport.airports.repository.CountryRepository;
import com.assessment.airport.airports.repository.RunwayRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/country")
class CountryController {

    @Autowired
    CountryRepository repository;

    @Autowired
    RunwayRepository runwayRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job readCSVFileJob;
    @Autowired
    Job readAirportCSVFileJob;
    @Autowired
    Job readRunwayCSVFileJob;

    @GetMapping("/runJobs")
    public String runJobs() {
        String returnMsg = "OK";
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(readCSVFileJob, jobParameters);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "readCSVFileJob KO";

        }

        try {
            jobLauncher.run(readAirportCSVFileJob, jobParameters);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "readAirportCSVFileJob KO";

        }

        try {
            jobLauncher.run(readRunwayCSVFileJob, jobParameters);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "readRunwayCSVFileJob KO";

        }

        return returnMsg;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAll() {
        try {
            List<Country> items = new ArrayList<Country>();

            repository.findAll().forEach(items::add);

            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/topten")
    public ResponseEntity<List<Object[]>> getTopTen() {
        try {

            List<Object[]> items = repository.topTenCountriesByAirportNumber();
            System.out.println(items);

            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<Country>> getAllByName(@PathVariable("name") String name) {
        try {
            List<Country> items = new ArrayList<Country>();
            System.out.println("Find strats iwth " + name);
            repository.findByNameStartsWith(name).forEach(items::add);
            System.out.println(items.size() + " items found");
            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/raunways/{code}")
    public ResponseEntity<List<Runway>> findRunwaysByCountryCode(@PathVariable("code") String code) {
        try {
            System.out.println("findRunwaysByCountryCode for: " + code);
            List<Long> itemsIds = repository.getRunwaysByCountryCode(code);
            List<Runway> items = runwayRepository.findAllById(itemsIds);
            System.out.println(items);
            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Country> create(@RequestBody Country item) {
        try {
            Country savedItem = repository.save(item);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}