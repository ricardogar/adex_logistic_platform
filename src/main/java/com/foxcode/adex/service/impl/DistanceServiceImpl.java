package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.DistanceService;
import com.foxcode.adex.domain.Distance;
import com.foxcode.adex.repository.DistanceRepository;
import com.foxcode.adex.repository.search.DistanceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Distance.
 */
@Service
@Transactional
public class DistanceServiceImpl implements DistanceService {

    private final Logger log = LoggerFactory.getLogger(DistanceServiceImpl.class);

    private final DistanceRepository distanceRepository;

    private final DistanceSearchRepository distanceSearchRepository;

    public DistanceServiceImpl(DistanceRepository distanceRepository, DistanceSearchRepository distanceSearchRepository) {
        this.distanceRepository = distanceRepository;
        this.distanceSearchRepository = distanceSearchRepository;
    }

    /**
     * Save a distance.
     *
     * @param distance the entity to save
     * @return the persisted entity
     */
    @Override
    public Distance save(Distance distance) {
        log.debug("Request to save Distance : {}", distance);        Distance result = distanceRepository.save(distance);
        distanceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the distances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Distance> findAll(Pageable pageable) {
        log.debug("Request to get all Distances");
        return distanceRepository.findAll(pageable);
    }


    /**
     * Get one distance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Distance> findOne(Long id) {
        log.debug("Request to get Distance : {}", id);
        return distanceRepository.findById(id);
    }

    /**
     * Delete the distance by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Distance : {}", id);
        distanceRepository.deleteById(id);
        distanceSearchRepository.deleteById(id);
    }

    /**
     * Search for the distance corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Distance> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Distances for query {}", query);
        return distanceSearchRepository.search(queryStringQuery(query), pageable);    }
}
