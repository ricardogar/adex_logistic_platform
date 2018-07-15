package com.foxcode.adex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A CompanyFactory.
 */
@Entity
@Table(name = "company_factory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companyfactory")
public class CompanyFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "factory")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transport> transports = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("factories")
    private CompanyMain company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public CompanyFactory city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public CompanyFactory postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public CompanyFactory street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public CompanyFactory houseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getFlatNumber() {
        return flatNumber;
    }

    public CompanyFactory flatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
        return this;
    }

    public void setFlatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CompanyFactory phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public CompanyFactory transports(Set<Transport> transports) {
        this.transports = transports;
        return this;
    }

    public CompanyFactory addTransport(Transport transport) {
        this.transports.add(transport);
        transport.setFactory(this);
        return this;
    }

    public CompanyFactory removeTransport(Transport transport) {
        this.transports.remove(transport);
        transport.setFactory(null);
        return this;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public CompanyMain getCompany() {
        return company;
    }

    public CompanyFactory company(CompanyMain companyMain) {
        this.company = companyMain;
        return this;
    }

    public void setCompany(CompanyMain companyMain) {
        this.company = companyMain;
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
        CompanyFactory companyFactory = (CompanyFactory) o;
        if (companyFactory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyFactory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyFactory{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", street='" + getStreet() + "'" +
            ", houseNumber=" + getHouseNumber() +
            ", flatNumber=" + getFlatNumber() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
