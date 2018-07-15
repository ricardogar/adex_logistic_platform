package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.TruckService;
import com.foxcode.adex.domain.Truck;
import com.foxcode.adex.repository.TruckRepository;
import com.foxcode.adex.repository.search.TruckSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Truck.
 */
@Service
@Transactional
public class TruckServiceImpl implements TruckService {

    private final Logger log = LoggerFactory.getLogger(TruckServiceImpl.class);

    private final TruckRepository truckRepository;

    private final TruckSearchRepository truckSearchRepository;

    public TruckServiceImpl(TruckRepository truckRepository, TruckSearchRepository truckSearchRepository) {
        this.truckRepository = truckRepository;
        this.truckSearchRepository = truckSearchRepository;
    }

    /**
     * Save a truck.
     *
     * @param truck the entity to save
     * @return the persisted entity
     */
    @Override
    public Truck save(Truck truck) {
        log.debug("Request to save Truck : {}", truck);
        Truck result = truckRepository.save(truck);
        truckSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the trucks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Truck> findAll(Pageable pageable) {
        log.debug("Request to get all Trucks");
        return truckRepository.findAll(pageable);
    }



    /**
     *  get all the trucks where Driver is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Truck> findAllWhereDriverIsNull() {
        log.debug("Request to get all trucks where Driver is null");
        return StreamSupport
            .stream(truckRepository.findAll().spliterator(), false)
            .filter(truck -> truck.getDriver() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one truck by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Truck> findOne(Long id) {
        log.debug("Request to get Truck : {}", id);
        return truckRepository.findById(id);
    }

    /**
     * Delete the truck by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Truck : {}", id);
        truckRepository.deleteById(id);
        truckSearchRepository.deleteById(id);
    }

    /**
     * Search for the truck corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Truck> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Trucks for query {}", query);
        return truckSearchRepository.search(queryStringQuery(query), pageable);    }
}
