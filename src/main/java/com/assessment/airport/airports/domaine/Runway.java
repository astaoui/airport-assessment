package com.assessment.airport.airports.domaine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

//To use the @Data annotation you should add the Lombok dependency.
@Data
@Entity
@NoArgsConstructor
public class Runway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long airport_ref;
    private String airport_ident;
    private int length_ft;
    private int width_ft;
    private String surface;
    private boolean lighted;
    private boolean closed;
    private String le_ident;
    private String le_latitude_deg;
    private String le_longitude_deg;
    private String le_elevation_ft;
    private String le_heading_degT;
    private String le_displaced_threshold_ft;
    private String he_ident;
    private String he_latitude_deg;
    private String he_longitude_deg;
    private String he_elevation_ft;
    private String he_heading_degT;
    private String he_displaced_threshold_ft;

}