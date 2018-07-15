package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.CompanyFactoryService;
import com.foxcode.adex.domain.CompanyFactory;
import com.foxcode.adex.repository.CompanyFactoryRepository;
import com.foxcode.adex.repository.search.CompanyFactorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CompanyFactory.
 */
@Service
@Transactional
public class CompanyFactoryServiceImpl implements CompanyFactoryService {

    private final Logger log = LoggerFactory.getLogger(CompanyFactoryServiceImpl.class);

    private final CompanyFactoryRepository companyFactoryRepository;

    private final CompanyFactorySearchRepository companyFactorySearchRepository;

    public CompanyFactoryServiceImpl(CompanyFactoryRepository companyFactoryRepository, CompanyFactorySearchRepository companyFactorySearchRepository) {
        this.companyFactoryRepository = companyFactoryRepository;
        this.companyFactorySearchRepository = companyFactorySearchRepository;
    }

    /**
     * Save a companyFactory.
     *
     * @param companyFactory the entity to save
     * @return the persisted entity
     */
    @Override
    public CompanyFactory save(CompanyFactory companyFactory) {
        log.debug("Request to save CompanyFactory : {}", companyFactory);        CompanyFactory result = companyFactoryRepository.save(companyFactory);
        companyFactorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the companyFactories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyFactory> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyFactories");
        return companyFactoryRepository.findAll(pageable);
    }


    /**
     * Get one companyFactory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyFactory> findOne(Long id) {
        log.debug("Request to get CompanyFactory : {}", id);
        return companyFactoryRepository.findById(id);
    }

    /**
     * Delete the companyFactory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanyFactory : {}", id);
        companyFactoryRepository.deleteById(id);
        companyFactorySearchRepository.deleteById(id);
    }

    /**
     * Search for the companyFactory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyFactory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CompanyFactories for query {}", query);
        return companyFactorySearchRepository.search(queryStringQuery(query), pageable);    }
}
