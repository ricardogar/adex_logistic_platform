package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.FuelService;
import com.foxcode.adex.domain.Fuel;
import com.foxcode.adex.repository.FuelRepository;
import com.foxcode.adex.repository.search.FuelSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Fuel.
 */
@Service
@Transactional
public class FuelServiceImpl implements FuelService {

    private final Logger log = LoggerFactory.getLogger(FuelServiceImpl.class);

    private final FuelRepository fuelRepository;

    private final FuelSearchRepository fuelSearchRepository;

    public FuelServiceImpl(FuelRepository fuelRepository, FuelSearchRepository fuelSearchRepository) {
        this.fuelRepository = fuelRepository;
        this.fuelSearchRepository = fuelSearchRepository;
    }

    /**
     * Save a fuel.
     *
     * @param fuel the entity to save
     * @return the persisted entity
     */
    @Override
    public Fuel save(Fuel fuel) {
        log.debug("Request to save Fuel : {}", fuel);        Fuel result = fuelRepository.save(fuel);
        fuelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the fuels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Fuel> findAll(Pageable pageable) {
        log.debug("Request to get all Fuels");
        return fuelRepository.findAll(pageable);
    }


    /**
     * Get one fuel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Fuel> findOne(Long id) {
        log.debug("Request to get Fuel : {}", id);
        return fuelRepository.findById(id);
    }

    /**
     * Delete the fuel by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fuel : {}", id);
        fuelRepository.deleteById(id);
        fuelSearchRepository.deleteById(id);
    }

    /**
     * Search for the fuel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Fuel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Fuels for query {}", query);
        return fuelSearchRepository.search(queryStringQuery(query), pageable);    }
}
