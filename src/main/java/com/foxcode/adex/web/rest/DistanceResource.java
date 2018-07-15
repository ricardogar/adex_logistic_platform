package com.foxcode.adex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxcode.adex.domain.Distance;
import com.foxcode.adex.service.DistanceService;
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
 * REST controller for managing Distance.
 */
@RestController
@RequestMapping("/api")
public class DistanceResource {

    private final Logger log = LoggerFactory.getLogger(DistanceResource.class);

    private static final String ENTITY_NAME = "distance";

    private final DistanceService distanceService;

    public DistanceResource(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    /**
     * POST  /distances : Create a new distance.
     *
     * @param distance the distance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new distance, or with status 400 (Bad Request) if the distance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/distances")
    @Timed
    public ResponseEntity<Distance> createDistance(@Valid @RequestBody Distance distance) throws URISyntaxException {
        log.debug("REST request to save Distance : {}", distance);
        if (distance.getId() != null) {
            throw new BadRequestAlertException("A new distance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Distance result = distanceService.save(distance);
        return ResponseEntity.created(new URI("/api/distances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /distances : Updates an existing distance.
     *
     * @param distance the distance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated distance,
     * or with status 400 (Bad Request) if the distance is not valid,
     * or with status 500 (Internal Server Error) if the distance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/distances")
    @Timed
    public ResponseEntity<Distance> updateDistance(@Valid @RequestBody Distance distance) throws URISyntaxException {
        log.debug("REST request to update Distance : {}", distance);
        if (distance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Distance result = distanceService.save(distance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /distances : get all the distances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of distances in body
     */
    @GetMapping("/distances")
    @Timed
    public ResponseEntity<List<Distance>> getAllDistances(Pageable pageable) {
        log.debug("REST request to get a page of Distances");
        Page<Distance> page = distanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/distances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /distances/:id : get the "id" distance.
     *
     * @param id the id of the distance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the distance, or with status 404 (Not Found)
     */
    @GetMapping("/distances/{id}")
    @Timed
    public ResponseEntity<Distance> getDistance(@PathVariable Long id) {
        log.debug("REST request to get Distance : {}", id);
        Optional<Distance> distance = distanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(distance);
    }

    /**
     * DELETE  /distances/:id : delete the "id" distance.
     *
     * @param id the id of the distance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/distances/{id}")
    @Timed
    public ResponseEntity<Void> deleteDistance(@PathVariable Long id) {
        log.debug("REST request to delete Distance : {}", id);
        distanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/distances?query=:query : search for the distance corresponding
     * to the query.
     *
     * @param query the query of the distance search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/distances")
    @Timed
    public ResponseEntity<List<Distance>> searchDistances(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Distances for query {}", query);
        Page<Distance> page = distanceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/distances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
