package com.foxcode.adex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxcode.adex.domain.Fuel;
import com.foxcode.adex.service.FuelService;
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
 * REST controller for managing Fuel.
 */
@RestController
@RequestMapping("/api")
public class FuelResource {

    private final Logger log = LoggerFactory.getLogger(FuelResource.class);

    private static final String ENTITY_NAME = "fuel";

    private final FuelService fuelService;

    public FuelResource(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    /**
     * POST  /fuels : Create a new fuel.
     *
     * @param fuel the fuel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fuel, or with status 400 (Bad Request) if the fuel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fuels")
    @Timed
    public ResponseEntity<Fuel> createFuel(@Valid @RequestBody Fuel fuel) throws URISyntaxException {
        log.debug("REST request to save Fuel : {}", fuel);
        if (fuel.getId() != null) {
            throw new BadRequestAlertException("A new fuel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fuel result = fuelService.save(fuel);
        return ResponseEntity.created(new URI("/api/fuels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fuels : Updates an existing fuel.
     *
     * @param fuel the fuel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fuel,
     * or with status 400 (Bad Request) if the fuel is not valid,
     * or with status 500 (Internal Server Error) if the fuel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fuels")
    @Timed
    public ResponseEntity<Fuel> updateFuel(@Valid @RequestBody Fuel fuel) throws URISyntaxException {
        log.debug("REST request to update Fuel : {}", fuel);
        if (fuel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fuel result = fuelService.save(fuel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fuel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fuels : get all the fuels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fuels in body
     */
    @GetMapping("/fuels")
    @Timed
    public ResponseEntity<List<Fuel>> getAllFuels(Pageable pageable) {
        log.debug("REST request to get a page of Fuels");
        Page<Fuel> page = fuelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fuels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fuels/:id : get the "id" fuel.
     *
     * @param id the id of the fuel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fuel, or with status 404 (Not Found)
     */
    @GetMapping("/fuels/{id}")
    @Timed
    public ResponseEntity<Fuel> getFuel(@PathVariable Long id) {
        log.debug("REST request to get Fuel : {}", id);
        Optional<Fuel> fuel = fuelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fuel);
    }

    /**
     * DELETE  /fuels/:id : delete the "id" fuel.
     *
     * @param id the id of the fuel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fuels/{id}")
    @Timed
    public ResponseEntity<Void> deleteFuel(@PathVariable Long id) {
        log.debug("REST request to delete Fuel : {}", id);
        fuelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fuels?query=:query : search for the fuel corresponding
     * to the query.
     *
     * @param query the query of the fuel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fuels")
    @Timed
    public ResponseEntity<List<Fuel>> searchFuels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Fuels for query {}", query);
        Page<Fuel> page = fuelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fuels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
