package com.assessment.airport.airports.domaine;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//To use the @Data annotation you should add the Lombok dependency.
@Data
@Entity
@NoArgsConstructor
@ToString
public class Country {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String continent;
    private String wikipediaLink;
    private String keywords;

    /*
     * @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval =
     * true) private List<Airport> airports = new ArrayList<>();
     */

}