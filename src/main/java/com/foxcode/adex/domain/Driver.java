package com.foxcode.adex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "pesel", nullable = false)
    private String pesel;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "post_code", nullable = false)
    private String postCode;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;

    @Column(name = "flat_number")
    private Integer flatNumber;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "medical_exam_date", nullable = false)
    private LocalDate medicalExamDate;

    @NotNull
    @Column(name = "driver_licence_date", nullable = false)
    private LocalDate driverLicenceDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private Truck truck;

    @OneToMany(mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transport> transports = new HashSet<>();

    @OneToMany(mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fuel> fuels = new HashSet<>();

    @OneToMany(mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Distance> distances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPesel() {
        return pesel;
    }

    public Driver pesel(String pesel) {
        this.pesel = pesel;
        return this;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getCity() {
        return city;
    }

    public Driver city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public Driver postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public Driver street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public Driver houseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getFlatNumber() {
        return flatNumber;
    }

    public Driver flatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
        return this;
    }

    public void setFlatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Driver phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getMedicalExamDate() {
        return medicalExamDate;
    }

    public Driver medicalExamDate(LocalDate medicalExamDate) {
        this.medicalExamDate = medicalExamDate;
        return this;
    }

    public void setMedicalExamDate(LocalDate medicalExamDate) {
        this.medicalExamDate = medicalExamDate;
    }

    public LocalDate getDriverLicenceDate() {
        return driverLicenceDate;
    }

    public Driver driverLicenceDate(LocalDate driverLicenceDate) {
        this.driverLicenceDate = driverLicenceDate;
        return this;
    }

    public void setDriverLicenceDate(LocalDate driverLicenceDate) {
        this.driverLicenceDate = driverLicenceDate;
    }

    public User getUser() {
        return user;
    }

    public Driver user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Truck getTruck() {
        return truck;
    }

    public Driver truck(Truck truck) {
        this.truck = truck;
        return this;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public Driver transports(Set<Transport> transports) {
        this.transports = transports;
        return this;
    }

    public Driver addTransport(Transport transport) {
        this.transports.add(transport);
        transport.setDriver(this);
        return this;
    }

    public Driver removeTransport(Transport transport) {
        this.transports.remove(transport);
        transport.setDriver(null);
        return this;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public Set<Fuel> getFuels() {
        return fuels;
    }

    public Driver fuels(Set<Fuel> fuels) {
        this.fuels = fuels;
        return this;
    }

    public Driver addFuel(Fuel fuel) {
        this.fuels.add(fuel);
        fuel.setDriver(this);
        return this;
    }

    public Driver removeFuel(Fuel fuel) {
        this.fuels.remove(fuel);
        fuel.setDriver(null);
        return this;
    }

    public void setFuels(Set<Fuel> fuels) {
        this.fuels = fuels;
    }

    public Set<Distance> getDistances() {
        return distances;
    }

    public Driver distances(Set<Distance> distances) {
        this.distances = distances;
        return this;
    }

    public Driver addDistance(Distance distance) {
        this.distances.add(distance);
        distance.setDriver(this);
        return this;
    }

    public Driver removeDistance(Distance distance) {
        this.distances.remove(distance);
        distance.setDriver(null);
        return this;
    }

    public void setDistances(Set<Distance> distances) {
        this.distances = distances;
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
        Driver driver = (Driver) o;
        if (driver.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), driver.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", pesel='" + getPesel() + "'" +
            ", city='" + getCity() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", street='" + getStreet() + "'" +
            ", houseNumber=" + getHouseNumber() +
            ", flatNumber=" + getFlatNumber() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", medicalExamDate='" + getMedicalExamDate() + "'" +
            ", driverLicenceDate='" + getDriverLicenceDate() + "'" +
            "}";
    }
}
