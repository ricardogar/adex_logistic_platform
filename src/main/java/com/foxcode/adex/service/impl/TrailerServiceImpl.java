package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.TrailerService;
import com.foxcode.adex.domain.Trailer;
import com.foxcode.adex.repository.TrailerRepository;
import com.foxcode.adex.repository.search.TrailerSearchRepository;
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
 * Service Implementation for managing Trailer.
 */
@Service
@Transactional
public class TrailerServiceImpl implements TrailerService {

    private final Logger log = LoggerFactory.getLogger(TrailerServiceImpl.class);

    private final TrailerRepository trailerRepository;

    private final TrailerSearchRepository trailerSearchRepository;

    public TrailerServiceImpl(TrailerRepository trailerRepository, TrailerSearchRepository trailerSearchRepository) {
        this.trailerRepository = trailerRepository;
        this.trailerSearchRepository = trailerSearchRepository;
    }

    /**
     * Save a trailer.
     *
     * @param trailer the entity to save
     * @return the persisted entity
     */
    @Override
    public Trailer save(Trailer trailer) {
        log.debug("Request to save Trailer : {}", trailer);        Trailer result = trailerRepository.save(trailer);
        trailerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the trailers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Trailer> findAll(Pageable pageable) {
        log.debug("Request to get all Trailers");
        return trailerRepository.findAll(pageable);
    }



    /**
     *  get all the trailers where Truck is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Trailer> findAllWhereTruckIsNull() {
        log.debug("Request to get all trailers where Truck is null");
        return StreamSupport
            .stream(trailerRepository.findAll().spliterator(), false)
            .filter(trailer -> trailer.getTruck() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one trailer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Trailer> findOne(Long id) {
        log.debug("Request to get Trailer : {}", id);
        return trailerRepository.findById(id);
    }

    /**
     * Delete the trailer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trailer : {}", id);
        trailerRepository.deleteById(id);
        trailerSearchRepository.deleteById(id);
    }

    /**
     * Search for the trailer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Trailer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Trailers for query {}", query);
        return trailerSearchRepository.search(queryStringQuery(query), pageable);    }
}
