package com.foxcode.adex.service;

import com.foxcode.adex.domain.Trailer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Trailer.
 */
public interface TrailerService {

    /**
     * Save a trailer.
     *
     * @param trailer the entity to save
     * @return the persisted entity
     */
    Trailer save(Trailer trailer);

    /**
     * Get all the trailers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Trailer> findAll(Pageable pageable);
    /**
     * Get all the TrailerDTO where Truck is null.
     *
     * @return the list of entities
     */
    List<Trailer> findAllWhereTruckIsNull();


    /**
     * Get the "id" trailer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Trailer> findOne(Long id);

    /**
     * Delete the "id" trailer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the trailer corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Trailer> search(String query, Pageable pageable);
}
