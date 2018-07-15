package com.foxcode.adex.service;

import com.foxcode.adex.domain.Truck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Truck.
 */
public interface TruckService {

    /**
     * Save a truck.
     *
     * @param truck the entity to save
     * @return the persisted entity
     */
    Truck save(Truck truck);

    /**
     * Get all the trucks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Truck> findAll(Pageable pageable);
    /**
     * Get all the TruckDTO where Driver is null.
     *
     * @return the list of entities
     */
    List<Truck> findAllWhereDriverIsNull();


    /**
     * Get the "id" truck.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Truck> findOne(Long id);

    /**
     * Delete the "id" truck.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the truck corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Truck> search(String query, Pageable pageable);
}
