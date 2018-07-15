package com.foxcode.adex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxcode.adex.domain.Trailer;
import com.foxcode.adex.service.TrailerService;
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
 * REST controller for managing Trailer.
 */
@RestController
@RequestMapping("/api")
public class TrailerResource {

    private final Logger log = LoggerFactory.getLogger(TrailerResource.class);

    private static final String ENTITY_NAME = "trailer";

    private final TrailerService trailerService;

    public TrailerResource(TrailerService trailerService) {
        this.trailerService = trailerService;
    }

    /**
     * POST  /trailers : Create a new trailer.
     *
     * @param trailer the trailer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trailer, or with status 400 (Bad Request) if the trailer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trailers")
    @Timed
    public ResponseEntity<Trailer> createTrailer(@Valid @RequestBody Trailer trailer) throws URISyntaxException {
        log.debug("REST request to save Trailer : {}", trailer);
        if (trailer.getId() != null) {
            throw new BadRequestAlertException("A new trailer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trailer result = trailerService.save(trailer);
        return ResponseEntity.created(new URI("/api/trailers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trailers : Updates an existing trailer.
     *
     * @param trailer the trailer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trailer,
     * or with status 400 (Bad Request) if the trailer is not valid,
     * or with status 500 (Internal Server Error) if the trailer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trailers")
    @Timed
    public ResponseEntity<Trailer> updateTrailer(@Valid @RequestBody Trailer trailer) throws URISyntaxException {
        log.debug("REST request to update Trailer : {}", trailer);
        if (trailer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Trailer result = trailerService.save(trailer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trailer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trailers : get all the trailers.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of trailers in body
     */
    @GetMapping("/trailers")
    @Timed
    public ResponseEntity<List<Trailer>> getAllTrailers(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("truck-is-null".equals(filter)) {
            log.debug("REST request to get all Trailers where truck is null");
            return new ResponseEntity<>(trailerService.findAllWhereTruckIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Trailers");
        Page<Trailer> page = trailerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trailers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trailers/:id : get the "id" trailer.
     *
     * @param id the id of the trailer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trailer, or with status 404 (Not Found)
     */
    @GetMapping("/trailers/{id}")
    @Timed
    public ResponseEntity<Trailer> getTrailer(@PathVariable Long id) {
        log.debug("REST request to get Trailer : {}", id);
        Optional<Trailer> trailer = trailerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trailer);
    }

    /**
     * DELETE  /trailers/:id : delete the "id" trailer.
     *
     * @param id the id of the trailer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trailers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrailer(@PathVariable Long id) {
        log.debug("REST request to delete Trailer : {}", id);
        trailerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/trailers?query=:query : search for the trailer corresponding
     * to the query.
     *
     * @param query the query of the trailer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/trailers")
    @Timed
    public ResponseEntity<List<Trailer>> searchTrailers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Trailers for query {}", query);
        Page<Trailer> page = trailerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/trailers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
