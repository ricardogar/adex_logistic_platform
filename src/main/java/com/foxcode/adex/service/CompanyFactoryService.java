package com.foxcode.adex.service;

import com.foxcode.adex.domain.CompanyFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CompanyFactory.
 */
public interface CompanyFactoryService {

    /**
     * Save a companyFactory.
     *
     * @param companyFactory the entity to save
     * @return the persisted entity
     */
    CompanyFactory save(CompanyFactory companyFactory);

    /**
     * Get all the companyFactories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompanyFactory> findAll(Pageable pageable);


    /**
     * Get the "id" companyFactory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CompanyFactory> findOne(Long id);

    /**
     * Delete the "id" companyFactory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the companyFactory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompanyFactory> search(String query, Pageable pageable);
}
