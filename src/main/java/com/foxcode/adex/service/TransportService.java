package com.foxcode.adex.service;

import com.foxcode.adex.domain.Transport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Transport.
 */
public interface TransportService {

    /**
     * Save a transport.
     *
     * @param transport the entity to save
     * @return the persisted entity
     */
    Transport save(Transport transport);

    /**
     * Get all the transports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Transport> findAll(Pageable pageable);


    /**
     * Get the "id" transport.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Transport> findOne(Long id);

    /**
     * Delete the "id" transport.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the transport corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Transport> search(String query, Pageable pageable);
}
