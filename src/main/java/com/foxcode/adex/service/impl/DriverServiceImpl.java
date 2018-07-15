package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.DriverService;
import com.foxcode.adex.domain.Driver;
import com.foxcode.adex.repository.DriverRepository;
import com.foxcode.adex.repository.search.DriverSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Driver.
 */
@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final DriverRepository driverRepository;

    private final DriverSearchRepository driverSearchRepository;

    public DriverServiceImpl(DriverRepository driverRepository, DriverSearchRepository driverSearchRepository) {
        this.driverRepository = driverRepository;
        this.driverSearchRepository = driverSearchRepository;
    }

    /**
     * Save a driver.
     *
     * @param driver the entity to save
     * @return the persisted entity
     */
    @Override
    public Driver save(Driver driver) {
        log.debug("Request to save Driver : {}", driver);
        Driver result = driverRepository.save(driver);
        driverSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the drivers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Driver> findAll(Pageable pageable) {
        log.debug("Request to get all Drivers");
        return driverRepository.findAll(pageable);
    }


    /**
     * Get one driver by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Driver> findOne(Long id) {
        log.debug("Request to get Driver : {}", id);
        return driverRepository.findById(id);
    }

    /**
     * Delete the driver by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Driver : {}", id);
        driverRepository.deleteById(id);
        driverSearchRepository.deleteById(id);
    }

    /**
     * Search for the driver corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Driver> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Drivers for query {}", query);
        return driverSearchRepository.search(queryStringQuery(query), pageable);    }
}
