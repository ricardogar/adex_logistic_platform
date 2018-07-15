package com.foxcode.adex.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Distance.
 */
@Entity
@Table(name = "distance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "distance")
public class Distance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "start_city", nullable = false)
    private String startCity;

    @NotNull
    @Column(name = "start_day", nullable = false)
    private LocalDate startDay;

    @NotNull
    @Column(name = "start_kilometers", nullable = false)
    private Long startKilometers;

    @NotNull
    @Column(name = "end_city", nullable = false)
    private String endCity;

    @NotNull
    @Column(name = "end_day", nullable = false)
    private LocalDate endDay;

    @NotNull
    @Column(name = "end_kilometers", nullable = false)
    private Long endKilometers;

    @ManyToOne
    @JsonIgnoreProperties("distances")
    private Driver driver;

    @ManyToOne
    @JsonIgnoreProperties("distances")
    private Truck truck;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartCity() {
        return startCity;
    }

    public Distance startCity(String startCity) {
        this.startCity = startCity;
        return this;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public Distance startDay(LocalDate startDay) {
        this.startDay = startDay;
        return this;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public Long getStartKilometers() {
        return startKilometers;
    }

    public Distance startKilometers(Long startKilometers) {
        this.startKilometers = startKilometers;
        return this;
    }

    public void setStartKilometers(Long startKilometers) {
        this.startKilometers = startKilometers;
    }

    public String getEndCity() {
        return endCity;
    }

    public Distance endCity(String endCity) {
        this.endCity = endCity;
        return this;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public Distance endDay(LocalDate endDay) {
        this.endDay = endDay;
        return this;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public Long getEndKilometers() {
        return endKilometers;
    }

    public Distance endKilometers(Long endKilometers) {
        this.endKilometers = endKilometers;
        return this;
    }

    public void setEndKilometers(Long endKilometers) {
        this.endKilometers = endKilometers;
    }

    public Driver getDriver() {
        return driver;
    }

    public Distance driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Truck getTruck() {
        return truck;
    }

    public Distance truck(Truck truck) {
        this.truck = truck;
        return this;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance = (Distance) o;
        if (distance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), distance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Distance{" +
            "id=" + getId() +
            ", startCity='" + getStartCity() + "'" +
            ", startDay='" + getStartDay() + "'" +
            ", startKilometers=" + getStartKilometers() +
            ", endCity='" + getEndCity() + "'" +
            ", endDay='" + getEndDay() + "'" +
            ", endKilometers=" + getEndKilometers() +
            "}";
    }
}
