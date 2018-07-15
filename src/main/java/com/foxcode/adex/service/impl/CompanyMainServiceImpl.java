package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.CompanyMainService;
import com.foxcode.adex.domain.CompanyMain;
import com.foxcode.adex.repository.CompanyMainRepository;
import com.foxcode.adex.repository.search.CompanyMainSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CompanyMain.
 */
@Service
@Transactional
public class CompanyMainServiceImpl implements CompanyMainService {

    private final Logger log = LoggerFactory.getLogger(CompanyMainServiceImpl.class);

    private final CompanyMainRepository companyMainRepository;

    private final CompanyMainSearchRepository companyMainSearchRepository;

    public CompanyMainServiceImpl(CompanyMainRepository companyMainRepository, CompanyMainSearchRepository companyMainSearchRepository) {
        this.companyMainRepository = companyMainRepository;
        this.companyMainSearchRepository = companyMainSearchRepository;
    }

    /**
     * Save a companyMain.
     *
     * @param companyMain the entity to save
     * @return the persisted entity
     */
    @Override
    public CompanyMain save(CompanyMain companyMain) {
        log.debug("Request to save CompanyMain : {}", companyMain);        CompanyMain result = companyMainRepository.save(companyMain);
        companyMainSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the companyMains.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyMain> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyMains");
        return companyMainRepository.findAll(pageable);
    }


    /**
     * Get one companyMain by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyMain> findOne(Long id) {
        log.debug("Request to get CompanyMain : {}", id);
        return companyMainRepository.findById(id);
    }

    /**
     * Delete the companyMain by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanyMain : {}", id);
        companyMainRepository.deleteById(id);
        companyMainSearchRepository.deleteById(id);
    }

    /**
     * Search for the companyMain corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyMain> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CompanyMains for query {}", query);
        return companyMainSearchRepository.search(queryStringQuery(query), pageable);    }
}
