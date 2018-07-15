package com.foxcode.adex.service.impl;

import com.foxcode.adex.service.TransportService;
import com.foxcode.adex.domain.Transport;
import com.foxcode.adex.repository.TransportRepository;
import com.foxcode.adex.repository.search.TransportSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Transport.
 */
@Service
@Transactional
public class TransportServiceImpl implements TransportService {

    private final Logger log = LoggerFactory.getLogger(TransportServiceImpl.class);

    private final TransportRepository transportRepository;

    private final TransportSearchRepository transportSearchRepository;

    public TransportServiceImpl(TransportRepository transportRepository, TransportSearchRepository transportSearchRepository) {
        this.transportRepository = transportRepository;
        this.transportSearchRepository = transportSearchRepository;
    }

    /**
     * Save a transport.
     *
     * @param transport the entity to save
     * @return the persisted entity
     */
    @Override
    public Transport save(Transport transport) {
        log.debug("Request to save Transport : {}", transport);        Transport result = transportRepository.save(transport);
        transportSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the transports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Transport> findAll(Pageable pageable) {
        log.debug("Request to get all Transports");
        return transportRepository.findAll(pageable);
    }


    /**
     * Get one transport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Transport> findOne(Long id) {
        log.debug("Request to get Transport : {}", id);
        return transportRepository.findById(id);
    }

    /**
     * Delete the transport by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transport : {}", id);
        transportRepository.deleteById(id);
        transportSearchRepository.deleteById(id);
    }

    /**
     * Search for the transport corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Transport> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Transports for query {}", query);
        return transportSearchRepository.search(queryStringQuery(query), pageable);    }
}
