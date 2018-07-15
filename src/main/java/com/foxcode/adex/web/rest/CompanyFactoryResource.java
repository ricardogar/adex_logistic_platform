package com.foxcode.adex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxcode.adex.domain.CompanyFactory;
import com.foxcode.adex.service.CompanyFactoryService;
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
 * REST controller for managing CompanyFactory.
 */
@RestController
@RequestMapping("/api")
public class CompanyFactoryResource {

    private final Logger log = LoggerFactory.getLogger(CompanyFactoryResource.class);

    private static final String ENTITY_NAME = "companyFactory";

    private final CompanyFactoryService companyFactoryService;

    public CompanyFactoryResource(CompanyFactoryService companyFactoryService) {
        this.companyFactoryService = companyFactoryService;
    }

    /**
     * POST  /company-factories : Create a new companyFactory.
     *
     * @param companyFactory the companyFactory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyFactory, or with status 400 (Bad Request) if the companyFactory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-factories")
    @Timed
    public ResponseEntity<CompanyFactory> createCompanyFactory(@Valid @RequestBody CompanyFactory companyFactory) throws URISyntaxException {
        log.debug("REST request to save CompanyFactory : {}", companyFactory);
        if (companyFactory.getId() != null) {
            throw new BadRequestAlertException("A new companyFactory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyFactory result = companyFactoryService.save(companyFactory);
        return ResponseEntity.created(new URI("/api/company-factories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-factories : Updates an existing companyFactory.
     *
     * @param companyFactory the companyFactory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyFactory,
     * or with status 400 (Bad Request) if the companyFactory is not valid,
     * or with status 500 (Internal Server Error) if the companyFactory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-factories")
    @Timed
    public ResponseEntity<CompanyFactory> updateCompanyFactory(@Valid @RequestBody CompanyFactory companyFactory) throws URISyntaxException {
        log.debug("REST request to update CompanyFactory : {}", companyFactory);
        if (companyFactory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompanyFactory result = companyFactoryService.save(companyFactory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyFactory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-factories : get all the companyFactories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyFactories in body
     */
    @GetMapping("/company-factories")
    @Timed
    public ResponseEntity<List<CompanyFactory>> getAllCompanyFactories(Pageable pageable) {
        log.debug("REST request to get a page of CompanyFactories");
        Page<CompanyFactory> page = companyFactoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-factories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-factories/:id : get the "id" companyFactory.
     *
     * @param id the id of the companyFactory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyFactory, or with status 404 (Not Found)
     */
    @GetMapping("/company-factories/{id}")
    @Timed
    public ResponseEntity<CompanyFactory> getCompanyFactory(@PathVariable Long id) {
        log.debug("REST request to get CompanyFactory : {}", id);
        Optional<CompanyFactory> companyFactory = companyFactoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyFactory);
    }

    /**
     * DELETE  /company-factories/:id : delete the "id" companyFactory.
     *
     * @param id the id of the companyFactory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-factories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyFactory(@PathVariable Long id) {
        log.debug("REST request to delete CompanyFactory : {}", id);
        companyFactoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-factories?query=:query : search for the companyFactory corresponding
     * to the query.
     *
     * @param query the query of the companyFactory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/company-factories")
    @Timed
    public ResponseEntity<List<CompanyFactory>> searchCompanyFactories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CompanyFactories for query {}", query);
        Page<CompanyFactory> page = companyFactoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/company-factories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
