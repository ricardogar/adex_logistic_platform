package com.foxcode.adex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CompanyMain.
 */
@Entity
@Table(name = "company_main")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companymain")
public class CompanyMain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nip", nullable = false)
    private String nip;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

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

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "comapny")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transport> transports = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyFactory> factories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public CompanyMain nip(String nip) {
        this.nip = nip;
        return this;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getName() {
        return name;
    }

    public CompanyMain name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public CompanyMain city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public CompanyMain postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public CompanyMain street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public CompanyMain houseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getFlatNumber() {
        return flatNumber;
    }

    public CompanyMain flatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
        return this;
    }

    public void setFlatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getEmail() {
        return email;
    }

    public CompanyMain email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public CompanyMain transports(Set<Transport> transports) {
        this.transports = transports;
        return this;
    }

    public CompanyMain addTransport(Transport transport) {
        this.transports.add(transport);
        transport.setComapny(this);
        return this;
    }

    public CompanyMain removeTransport(Transport transport) {
        this.transports.remove(transport);
        transport.setComapny(null);
        return this;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public Set<CompanyFactory> getFactories() {
        return factories;
    }

    public CompanyMain factories(Set<CompanyFactory> companyFactories) {
        this.factories = companyFactories;
        return this;
    }

    public CompanyMain addFactory(CompanyFactory companyFactory) {
        this.factories.add(companyFactory);
        companyFactory.setCompany(this);
        return this;
    }

    public CompanyMain removeFactory(CompanyFactory companyFactory) {
        this.factories.remove(companyFactory);
        companyFactory.setCompany(null);
        return this;
    }

    public void setFactories(Set<CompanyFactory> companyFactories) {
        this.factories = companyFactories;
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
        CompanyMain companyMain = (CompanyMain) o;
        if (companyMain.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyMain.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyMain{" +
            "id=" + getId() +
            ", nip='" + getNip() + "'" +
            ", name='" + getName() + "'" +
            ", city='" + getCity() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", street='" + getStreet() + "'" +
            ", houseNumber=" + getHouseNumber() +
            ", flatNumber=" + getFlatNumber() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
