package com.foxcode.adex.service;

import com.foxcode.adex.domain.CompanyMain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CompanyMain.
 */
public interface CompanyMainService {

    /**
     * Save a companyMain.
     *
     * @param companyMain the entity to save
     * @return the persisted entity
     */
    CompanyMain save(CompanyMain companyMain);

    /**
     * Get all the companyMains.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompanyMain> findAll(Pageable pageable);


    /**
     * Get the "id" companyMain.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CompanyMain> findOne(Long id);

    /**
     * Delete the "id" companyMain.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the companyMain corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompanyMain> search(String query, Pageable pageable);
}
