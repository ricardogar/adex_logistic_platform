package com.foxcode.adex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxcode.adex.domain.CompanyMain;
import com.foxcode.adex.service.CompanyMainService;
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
 * REST controller for managing CompanyMain.
 */
@RestController
@RequestMapping("/api")
public class CompanyMainResource {

    private final Logger log = LoggerFactory.getLogger(CompanyMainResource.class);

    private static final String ENTITY_NAME = "companyMain";

    private final CompanyMainService companyMainService;

    public CompanyMainResource(CompanyMainService companyMainService) {
        this.companyMainService = companyMainService;
    }

    /**
     * POST  /company-mains : Create a new companyMain.
     *
     * @param companyMain the companyMain to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyMain, or with status 400 (Bad Request) if the companyMain has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-mains")
    @Timed
    public ResponseEntity<CompanyMain> createCompanyMain(@Valid @RequestBody CompanyMain companyMain) throws URISyntaxException {
        log.debug("REST request to save CompanyMain : {}", companyMain);
        if (companyMain.getId() != null) {
            throw new BadRequestAlertException("A new companyMain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyMain result = companyMainService.save(companyMain);
        return ResponseEntity.created(new URI("/api/company-mains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-mains : Updates an existing companyMain.
     *
     * @param companyMain the companyMain to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyMain,
     * or with status 400 (Bad Request) if the companyMain is not valid,
     * or with status 500 (Internal Server Error) if the companyMain couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-mains")
    @Timed
    public ResponseEntity<CompanyMain> updateCompanyMain(@Valid @RequestBody CompanyMain companyMain) throws URISyntaxException {
        log.debug("REST request to update CompanyMain : {}", companyMain);
        if (companyMain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompanyMain result = companyMainService.save(companyMain);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyMain.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-mains : get all the companyMains.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyMains in body
     */
    @GetMapping("/company-mains")
    @Timed
    public ResponseEntity<List<CompanyMain>> getAllCompanyMains(Pageable pageable) {
        log.debug("REST request to get a page of CompanyMains");
        Page<CompanyMain> page = companyMainService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-mains");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-mains/:id : get the "id" companyMain.
     *
     * @param id the id of the companyMain to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyMain, or with status 404 (Not Found)
     */
    @GetMapping("/company-mains/{id}")
    @Timed
    public ResponseEntity<CompanyMain> getCompanyMain(@PathVariable Long id) {
        log.debug("REST request to get CompanyMain : {}", id);
        Optional<CompanyMain> companyMain = companyMainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyMain);
    }

    /**
     * DELETE  /company-mains/:id : delete the "id" companyMain.
     *
     * @param id the id of the companyMain to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-mains/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyMain(@PathVariable Long id) {
        log.debug("REST request to delete CompanyMain : {}", id);
        companyMainService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-mains?query=:query : search for the companyMain corresponding
     * to the query.
     *
     * @param query the query of the companyMain search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/company-mains")
    @Timed
    public ResponseEntity<List<CompanyMain>> searchCompanyMains(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CompanyMains for query {}", query);
        Page<CompanyMain> page = companyMainService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/company-mains");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
