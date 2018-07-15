package com.foxcode.adex.service;

import com.foxcode.adex.domain.Driver;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Driver.
 */
public interface DriverService {

    /**
     * Save a driver.
     *
     * @param driver the entity to save
     * @return the persisted entity
     */
    Driver save(Driver driver);

    /**
     * Get all the drivers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Driver> findAll(Pageable pageable);


    /**
     * Get the "id" driver.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Driver> findOne(Long id);

    /**
     * Delete the "id" driver.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the driver corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Driver> search(String query, Pageable pageable);
}
