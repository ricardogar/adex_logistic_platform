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
 * A Fuel.
 */
@Entity
@Table(name = "fuel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fuel")
public class Fuel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "refuel_data", nullable = false)
    private LocalDate refuelData;

    @NotNull
    @Column(name = "amount_fuel", nullable = false)
    private Long amountFuel;

    @NotNull
    @Column(name = "kilometers_state", nullable = false)
    private Long kilometersState;

    @NotNull
    @Column(name = "fuel_price", nullable = false)
    private Double fuelPrice;

    @ManyToOne
    @JsonIgnoreProperties("fuels")
    private Driver driver;

    @ManyToOne
    @JsonIgnoreProperties("fuels")
    private Truck truck;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRefuelData() {
        return refuelData;
    }

    public Fuel refuelData(LocalDate refuelData) {
        this.refuelData = refuelData;
        return this;
    }

    public void setRefuelData(LocalDate refuelData) {
        this.refuelData = refuelData;
    }

    public Long getAmountFuel() {
        return amountFuel;
    }

    public Fuel amountFuel(Long amountFuel) {
        this.amountFuel = amountFuel;
        return this;
    }

    public void setAmountFuel(Long amountFuel) {
        this.amountFuel = amountFuel;
    }

    public Long getKilometersState() {
        return kilometersState;
    }

    public Fuel kilometersState(Long kilometersState) {
        this.kilometersState = kilometersState;
        return this;
    }

    public void setKilometersState(Long kilometersState) {
        this.kilometersState = kilometersState;
    }

    public Double getFuelPrice() {
        return fuelPrice;
    }

    public Fuel fuelPrice(Double fuelPrice) {
        this.fuelPrice = fuelPrice;
        return this;
    }

    public void setFuelPrice(Double fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public Driver getDriver() {
        return driver;
    }

    public Fuel driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Truck getTruck() {
        return truck;
    }

    public Fuel truck(Truck truck) {
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
        Fuel fuel = (Fuel) o;
        if (fuel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fuel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Fuel{" +
            "id=" + getId() +
            ", refuelData='" + getRefuelData() + "'" +
            ", amountFuel=" + getAmountFuel() +
            ", kilometersState=" + getKilometersState() +
            ", fuelPrice=" + getFuelPrice() +
            "}";
    }
}
