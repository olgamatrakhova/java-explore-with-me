package ru.practicum.event.location.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                "}";
    }
}