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

import com.foxcode.adex.domain.enumeration.TrailerType;

/**
 * A Trailer.
 */
@Entity
@Table(name = "trailer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "trailer")
public class Trailer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "plate", nullable = false)
    private String plate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "trailer_type", nullable = false)
    private TrailerType trailerType;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "production_year", nullable = false)
    private Integer productionYear;

    @NotNull
    @Column(name = "technical_exam_date", nullable = false)
    private LocalDate technicalExamDate;

    @NotNull
    @Column(name = "supervision_exam_date", nullable = false)
    private LocalDate supervisionExamDate;

    @NotNull
    @Column(name = "max_capacity", nullable = false)
    private Long maxCapacity;

    @OneToMany(mappedBy = "trailer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transport> transports = new HashSet<>();

    @OneToOne(mappedBy = "trailer")
    @JsonIgnore
    private Truck truck;

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

    public Trailer plate(String plate) {
        this.plate = plate;
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public TrailerType getTrailerType() {
        return trailerType;
    }

    public Trailer trailerType(TrailerType trailerType) {
        this.trailerType = trailerType;
        return this;
    }

    public void setTrailerType(TrailerType trailerType) {
        this.trailerType = trailerType;
    }

    public String getBrand() {
        return brand;
    }

    public Trailer brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public Trailer productionYear(Integer productionYear) {
        this.productionYear = productionYear;
        return this;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public LocalDate getTechnicalExamDate() {
        return technicalExamDate;
    }

    public Trailer technicalExamDate(LocalDate technicalExamDate) {
        this.technicalExamDate = technicalExamDate;
        return this;
    }

    public void setTechnicalExamDate(LocalDate technicalExamDate) {
        this.technicalExamDate = technicalExamDate;
    }

    public LocalDate getSupervisionExamDate() {
        return supervisionExamDate;
    }

    public Trailer supervisionExamDate(LocalDate supervisionExamDate) {
        this.supervisionExamDate = supervisionExamDate;
        return this;
    }

    public void setSupervisionExamDate(LocalDate supervisionExamDate) {
        this.supervisionExamDate = supervisionExamDate;
    }

    public Long getMaxCapacity() {
        return maxCapacity;
    }

    public Trailer maxCapacity(Long maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public void setMaxCapacity(Long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public Trailer transports(Set<Transport> transports) {
        this.transports = transports;
        return this;
    }

    public Trailer addTransport(Transport transport) {
        this.transports.add(transport);
        transport.setTrailer(this);
        return this;
    }

    public Trailer removeTransport(Transport transport) {
        this.transports.remove(transport);
        transport.setTrailer(null);
        return this;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public Truck getTruck() {
        return truck;
    }

    public Trailer truck(Truck truck) {
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
        Trailer trailer = (Trailer) o;
        if (trailer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trailer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trailer{" +
            "id=" + getId() +
            ", plate='" + getPlate() + "'" +
            ", trailerType='" + getTrailerType() + "'" +
            ", brand='" + getBrand() + "'" +
            ", productionYear=" + getProductionYear() +
            ", technicalExamDate='" + getTechnicalExamDate() + "'" +
            ", supervisionExamDate='" + getSupervisionExamDate() + "'" +
            ", maxCapacity=" + getMaxCapacity() +
            "}";
    }
}
