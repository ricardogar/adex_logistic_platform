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

import com.foxcode.adex.domain.enumeration.EmissionStandard;

/**
 * A Truck.
 */
@Entity
@Table(name = "truck")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "truck")
public class Truck implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "plate", nullable = false)
    private String plate;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "production_year", nullable = false)
    private Integer productionYear;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "emission_standard", nullable = false)
    private EmissionStandard emissionStandard;

    @NotNull
    @Column(name = "horse_power", nullable = false)
    private Integer horsePower;

    @NotNull
    @Column(name = "fuel_tank", nullable = false)
    private Integer fuelTank;

    @NotNull
    @Column(name = "technical_exam_date", nullable = false)
    private LocalDate technicalExamDate;

    @NotNull
    @Column(name = "compressor", nullable = false)
    private Boolean compressor;

    @NotNull
    @Column(name = "hydraulics", nullable = false)
    private Boolean hydraulics;

    @NotNull
    @Column(name = "gps", nullable = false)
    private Boolean gps;

    @NotNull
    @Column(name = "international_license", nullable = false)
    private Boolean internationalLicense;

    @OneToOne
    @JoinColumn(unique = true)
    private Trailer trailer;

    @OneToMany(mappedBy = "truck")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transport> transports = new HashSet<>();

    @OneToOne(mappedBy = "truck")
    @JsonIgnore
    private Driver driver;

    @OneToMany(mappedBy = "truck")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fuel> fuels = new HashSet<>();

    @OneToMany(mappedBy = "truck")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Distance> distances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public Truck plate(String plate) {
        this.plate = plate;
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public Truck brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public Truck productionYear(Integer productionYear) {
        this.productionYear = productionYear;
        return this;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public EmissionStandard getEmissionStandard() {
        return emissionStandard;
    }

    public Truck emissionStandard(EmissionStandard emissionStandard) {
        this.emissionStandard = emissionStandard;
        return this;
    }

    public void setEmissionStandard(EmissionStandard emissionStandard) {
        this.emissionStandard = emissionStandard;
    }

    public Integer getHorsePower() {
        return horsePower;
    }

    public Truck horsePower(Integer horsePower) {
        this.horsePower = horsePower;
        return this;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }

    public Integer getFuelTank() {
        return fuelTank;
    }

    public Truck fuelTank(Integer fuelTank) {
        this.fuelTank = fuelTank;
        return this;
    }

    public void setFuelTank(Integer fuelTank) {
        this.fuelTank = fuelTank;
    }

    public LocalDate getTechnicalExamDate() {
        return technicalExamDate;
    }

    public Truck technicalExamDate(LocalDate technicalExamDate) {
        this.technicalExamDate = technicalExamDate;
        return this;
    }

    public void setTechnicalExamDate(LocalDate technicalExamDate) {
        this.technicalExamDate = technicalExamDate;
    }

    public Boolean isCompressor() {
        return compressor;
    }

    public Truck compressor(Boolean compressor) {
        this.compressor = compressor;
        return this;
    }

    public void setCompressor(Boolean compressor) {
        this.compressor = compressor;
    }

    public Boolean isHydraulics() {
        return hydraulics;
    }

    public Truck hydraulics(Boolean hydraulics) {
        this.hydraulics = hydraulics;
        return this;
    }

    public void setHydraulics(Boolean hydraulics) {
        this.hydraulics = hydraulics;
    }

    public Boolean isGps() {
        return gps;
    }

    public Truck gps(Boolean gps) {
        this.gps = gps;
        return this;
    }

    public void setGps(Boolean gps) {
        this.gps = gps;
    }

    public Boolean isInternationalLicense() {
        return internationalLicense;
    }

    public Truck internationalLicense(Boolean internationalLicense) {
        this.internationalLicense = internationalLicense;
        return this;
    }

    public void setInternationalLicense(Boolean internationalLicense) {
        this.internationalLicense = internationalLicense;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public Truck trailer(Trailer trailer) {
        this.trailer = trailer;
        return this;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public Truck transports(Set<Transport> transports) {
        this.transports = transports;
        return this;
    }

    public Truck addTransport(Transport transport) {
        this.transports.add(transport);
        transport.setTruck(this);
        return this;
    }

    public Truck removeTransport(Transport transport) {
        this.transports.remove(transport);
        transport.setTruck(null);
        return this;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public Driver getDriver() {
        return driver;
    }

    public Truck driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Set<Fuel> getFuels() {
        return fuels;
    }

    public Truck fuels(Set<Fuel> fuels) {
        this.fuels = fuels;
        return this;
    }

    public Truck addFuel(Fuel fuel) {
        this.fuels.add(fuel);
        fuel.setTruck(this);
        return this;
    }

    public Truck removeFuel(Fuel fuel) {
        this.fuels.remove(fuel);
        fuel.setTruck(null);
        return this;
    }

    public void setFuels(Set<Fuel> fuels) {
        this.fuels = fuels;
    }

    public Set<Distance> getDistances() {
        return distances;
    }

    public Truck distances(Set<Distance> distances) {
        this.distances = distances;
        return this;
    }

    public Truck addDistance(Distance distance) {
        this.distances.add(distance);
        distance.setTruck(this);
        return this;
    }

    public Truck removeDistance(Distance distance) {
        this.distances.remove(distance);
        distance.setTruck(null);
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
        Truck truck = (Truck) o;
        if (truck.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), truck.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Truck{" +
            "id=" + getId() +
            ", plate='" + getPlate() + "'" +
            ", brand='" + getBrand() + "'" +
            ", productionYear=" + getProductionYear() +
            ", emissionStandard='" + getEmissionStandard() + "'" +
            ", horsePower=" + getHorsePower() +
            ", fuelTank=" + getFuelTank() +
            ", technicalExamDate='" + getTechnicalExamDate() + "'" +
            ", compressor='" + isCompressor() + "'" +
            ", hydraulics='" + isHydraulics() + "'" +
            ", gps='" + isGps() + "'" +
            ", internationalLicense='" + isInternationalLicense() + "'" +
            "}";
    }
}
