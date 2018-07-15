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

import com.foxcode.adex.domain.enumeration.TransportStatus;

/**
 * A Transport.
 */
@Entity
@Table(name = "transport")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "transport")
public class Transport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransportStatus status;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "post_code", nullable = false)
    private String postCode;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "planned_delivery_date")
    private LocalDate plannedDeliveryDate;

    @NotNull
    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @NotNull
    @Column(name = "changing_place_unloading", nullable = false)
    private Boolean changingPlaceUnloading;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JsonIgnoreProperties("transports")
    private Driver driver;

    @ManyToOne
    @JsonIgnoreProperties("transports")
    private Truck truck;

    @ManyToOne
    @JsonIgnoreProperties("transports")
    private Trailer trailer;

    @ManyToOne
    @JsonIgnoreProperties("transports")
    private CompanyFactory factory;

    @ManyToOne
    @JsonIgnoreProperties("transports")
    private CompanyMain comapny;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Transport orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public Transport createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public TransportStatus getStatus() {
        return status;
    }

    public Transport status(TransportStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TransportStatus status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public Transport city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public Transport postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public Transport street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public Transport houseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public Transport firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Transport lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Transport phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getPlannedDeliveryDate() {
        return plannedDeliveryDate;
    }

    public Transport plannedDeliveryDate(LocalDate plannedDeliveryDate) {
        this.plannedDeliveryDate = plannedDeliveryDate;
        return this;
    }

    public void setPlannedDeliveryDate(LocalDate plannedDeliveryDate) {
        this.plannedDeliveryDate = plannedDeliveryDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public Transport deliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean isChangingPlaceUnloading() {
        return changingPlaceUnloading;
    }

    public Transport changingPlaceUnloading(Boolean changingPlaceUnloading) {
        this.changingPlaceUnloading = changingPlaceUnloading;
        return this;
    }

    public void setChangingPlaceUnloading(Boolean changingPlaceUnloading) {
        this.changingPlaceUnloading = changingPlaceUnloading;
    }

    public String getComments() {
        return comments;
    }

    public Transport comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Driver getDriver() {
        return driver;
    }

    public Transport driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Truck getTruck() {
        return truck;
    }

    public Transport truck(Truck truck) {
        this.truck = truck;
        return this;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public Transport trailer(Trailer trailer) {
        this.trailer = trailer;
        return this;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public CompanyFactory getFactory() {
        return factory;
    }

    public Transport factory(CompanyFactory companyFactory) {
        this.factory = companyFactory;
        return this;
    }

    public void setFactory(CompanyFactory companyFactory) {
        this.factory = companyFactory;
    }

    public CompanyMain getComapny() {
        return comapny;
    }

    public Transport comapny(CompanyMain companyMain) {
        this.comapny = companyMain;
        return this;
    }

    public void setComapny(CompanyMain companyMain) {
        this.comapny = companyMain;
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
        Transport transport = (Transport) o;
        if (transport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transport{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", city='" + getCity() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", street='" + getStreet() + "'" +
            ", houseNumber=" + getHouseNumber() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", plannedDeliveryDate='" + getPlannedDeliveryDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", changingPlaceUnloading='" + isChangingPlaceUnloading() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
