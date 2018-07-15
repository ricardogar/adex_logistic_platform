package com.foxcode.adex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxcode.adex.domain.Transport;
import com.foxcode.adex.service.TransportService;
import com.foxcode.adex.web.rest.errors.BadRequestAlertException;
import com.foxcode.adex.web.rest.util.HeaderUtil;
import com.foxcode.adex.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Transport.
 */
@RestController
@RequestMapping("/api")
public class TransportResource {

    private final Logger log = LoggerFactory.getLogger(TransportResource.class);

    private static final String ENTITY_NAME = "transport";

    private final TransportService transportService;

    public TransportResource(TransportService transportService) {
        this.transportService = transportService;
    }

    /**
     * POST  /transports : Create a new transport.
     *
     * @param transport the transport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transport, or with status 400 (Bad Request) if the transport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transports")
    @Timed
    public ResponseEntity<Transport> createTransport(@Valid @RequestBody Transport transport) throws URISyntaxException {
        log.debug("REST request to save Transport : {}", transport);
        if (transport.getId() != null) {
            throw new BadRequestAlertException("A new transport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transport result = transportService.save(transport);
        return ResponseEntity.created(new URI("/api/transports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transports : Updates an existing transport.
     *
     * @param transport the transport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transport,
     * or with status 400 (Bad Request) if the transport is not valid,
     * or with status 500 (Internal Server Error) if the transport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transports")
    @Timed
    public ResponseEntity<Transport> updateTransport(@Valid @RequestBody Transport transport) throws URISyntaxException {
        log.debug("REST request to update Transport : {}", transport);
        if (transport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Transport result = transportService.save(transport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transports : get all the transports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transports in body
     */
    @GetMapping("/transports")
    @Timed
    public ResponseEntity<List<Transport>> getAllTransports(Pageable pageable) {
        log.debug("REST request to get a page of Transports");
        Page<Transport> page = transportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transports/:id : get the "id" transport.
     *
     * @param id the id of the transport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transport, or with status 404 (Not Found)
     */
    @GetMapping("/transports/{id}")
    @Timed
    public ResponseEntity<Transport> getTransport(@PathVariable Long id) {
        log.debug("REST request to get Transport : {}", id);
        Optional<Transport> transport = transportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transport);
    }

    /**
     * DELETE  /transports/:id : delete the "id" transport.
     *
     * @param id the id of the transport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transports/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransport(@PathVariable Long id) {
        log.debug("REST request to delete Transport : {}", id);
        transportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transports?query=:query : search for the transport corresponding
     * to the query.
     *
     * @param query the query of the transport search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transports")
    @Timed
    public ResponseEntity<List<Transport>> searchTransports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Transports for query {}", query);
        Page<Transport> page = transportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
