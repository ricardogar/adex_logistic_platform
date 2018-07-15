package com.foxcode.adex.service;

import com.foxcode.adex.domain.Fuel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Fuel.
 */
public interface FuelService {

    /**
     * Save a fuel.
     *
     * @param fuel the entity to save
     * @return the persisted entity
     */
    Fuel save(Fuel fuel);

    /**
     * Get all the fuels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Fuel> findAll(Pageable pageable);


    /**
     * Get the "id" fuel.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Fuel> findOne(Long id);

    /**
     * Delete the "id" fuel.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the fuel corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Fuel> search(String query, Pageable pageable);
}
