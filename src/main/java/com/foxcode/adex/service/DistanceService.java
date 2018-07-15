package com.foxcode.adex.service;

import com.foxcode.adex.domain.Distance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Distance.
 */
public interface DistanceService {

    /**
     * Save a distance.
     *
     * @param distance the entity to save
     * @return the persisted entity
     */
    Distance save(Distance distance);

    /**
     * Get all the distances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Distance> findAll(Pageable pageable);


    /**
     * Get the "id" distance.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Distance> findOne(Long id);

    /**
     * Delete the "id" distance.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the distance corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Distance> search(String query, Pageable pageable);
}
