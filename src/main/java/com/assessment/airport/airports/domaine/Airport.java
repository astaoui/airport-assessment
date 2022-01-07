package com.assessment.airport.airports.domaine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

//To use the @Data annotation you should add the Lombok dependency.
@Data
@Entity
@NoArgsConstructor
public class Airport {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ident;
    private String type;
    private String name;
    private String latitude_deg;
    private String longitude_deg;
    private String elevation_ft;
    private String continent;
    private String iso_country;
    private String iso_region;
    private String municipality;
    private String scheduled_service;
    private String gps_code;
    private String iata_code;
    private String local_code;
    private String home_link;
    private String wikipedia_link;
    @Column(columnDefinition = "TEXT")
    private String keywords;

    /*
     * @ManyToOne(fetch = FetchType.LAZY) private Country country;
     * 
     * @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL, orphanRemoval =
     * true) private List<Runway> airports = new ArrayList<>();
     */

}