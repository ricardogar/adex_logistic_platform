package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.Trailer;
import com.foxcode.adex.repository.TrailerRepository;
import com.foxcode.adex.repository.search.TrailerSearchRepository;
import com.foxcode.adex.service.TrailerService;
import com.foxcode.adex.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.foxcode.adex.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.foxcode.adex.domain.enumeration.TrailerType;
/**
 * Test class for the TrailerResource REST controller.
 *
 * @see TrailerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class TrailerResourceIntTest {

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

    private static final TrailerType DEFAULT_TRAILER_TYPE = TrailerType.CUARTAINSIDER;
    private static final TrailerType UPDATED_TRAILER_TYPE = TrailerType.LOWLOADER;

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCTION_YEAR = 1;
    private static final Integer UPDATED_PRODUCTION_YEAR = 2;

    private static final LocalDate DEFAULT_TECHNICAL_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TECHNICAL_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SUPERVISION_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUPERVISION_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_MAX_CAPACITY = 1L;
    private static final Long UPDATED_MAX_CAPACITY = 2L;

    @Autowired
    private TrailerRepository trailerRepository;

    

    @Autowired
    private TrailerService trailerService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.TrailerSearchRepositoryMockConfiguration
     */
    @Autowired
    private TrailerSearchRepository mockTrailerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrailerMockMvc;

    private Trailer trailer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrailerResource trailerResource = new TrailerResource(trailerService);
        this.restTrailerMockMvc = MockMvcBuilders.standaloneSetup(trailerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trailer createEntity(EntityManager em) {
        Trailer trailer = new Trailer()
            .plate(DEFAULT_PLATE)
            .trailerType(DEFAULT_TRAILER_TYPE)
            .brand(DEFAULT_BRAND)
            .productionYear(DEFAULT_PRODUCTION_YEAR)
            .technicalExamDate(DEFAULT_TECHNICAL_EXAM_DATE)
            .supervisionExamDate(DEFAULT_SUPERVISION_EXAM_DATE)
            .maxCapacity(DEFAULT_MAX_CAPACITY);
        return trailer;
    }

    @Before
    public void initTest() {
        trailer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrailer() throws Exception {
        int databaseSizeBeforeCreate = trailerRepository.findAll().size();

        // Create the Trailer
        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isCreated());

        // Validate the Trailer in the database
        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeCreate + 1);
        Trailer testTrailer = trailerList.get(trailerList.size() - 1);
        assertThat(testTrailer.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testTrailer.getTrailerType()).isEqualTo(DEFAULT_TRAILER_TYPE);
        assertThat(testTrailer.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testTrailer.getProductionYear()).isEqualTo(DEFAULT_PRODUCTION_YEAR);
        assertThat(testTrailer.getTechnicalExamDate()).isEqualTo(DEFAULT_TECHNICAL_EXAM_DATE);
        assertThat(testTrailer.getSupervisionExamDate()).isEqualTo(DEFAULT_SUPERVISION_EXAM_DATE);
        assertThat(testTrailer.getMaxCapacity()).isEqualTo(DEFAULT_MAX_CAPACITY);

        // Validate the Trailer in Elasticsearch
        verify(mockTrailerSearchRepository, times(1)).save(testTrailer);
    }

    @Test
    @Transactional
    public void createTrailerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trailerRepository.findAll().size();

        // Create the Trailer with an existing ID
        trailer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        // Validate the Trailer in the database
        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Trailer in Elasticsearch
        verify(mockTrailerSearchRepository, times(0)).save(trailer);
    }

    @Test
    @Transactional
    public void checkPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setPlate(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTrailerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setTrailerType(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setBrand(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductionYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setProductionYear(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTechnicalExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setTechnicalExamDate(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupervisionExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setSupervisionExamDate(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailerRepository.findAll().size();
        // set the field null
        trailer.setMaxCapacity(null);

        // Create the Trailer, which fails.

        restTrailerMockMvc.perform(post("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrailers() throws Exception {
        // Initialize the database
        trailerRepository.saveAndFlush(trailer);

        // Get all the trailerList
        restTrailerMockMvc.perform(get("/api/trailers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trailer.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
            .andExpect(jsonPath("$.[*].trailerType").value(hasItem(DEFAULT_TRAILER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].technicalExamDate").value(hasItem(DEFAULT_TECHNICAL_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].supervisionExamDate").value(hasItem(DEFAULT_SUPERVISION_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxCapacity").value(hasItem(DEFAULT_MAX_CAPACITY.intValue())));
    }
    

    @Test
    @Transactional
    public void getTrailer() throws Exception {
        // Initialize the database
        trailerRepository.saveAndFlush(trailer);

        // Get the trailer
        restTrailerMockMvc.perform(get("/api/trailers/{id}", trailer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trailer.getId().intValue()))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE.toString()))
            .andExpect(jsonPath("$.trailerType").value(DEFAULT_TRAILER_TYPE.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.productionYear").value(DEFAULT_PRODUCTION_YEAR))
            .andExpect(jsonPath("$.technicalExamDate").value(DEFAULT_TECHNICAL_EXAM_DATE.toString()))
            .andExpect(jsonPath("$.supervisionExamDate").value(DEFAULT_SUPERVISION_EXAM_DATE.toString()))
            .andExpect(jsonPath("$.maxCapacity").value(DEFAULT_MAX_CAPACITY.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTrailer() throws Exception {
        // Get the trailer
        restTrailerMockMvc.perform(get("/api/trailers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrailer() throws Exception {
        // Initialize the database
        trailerService.save(trailer);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTrailerSearchRepository);

        int databaseSizeBeforeUpdate = trailerRepository.findAll().size();

        // Update the trailer
        Trailer updatedTrailer = trailerRepository.findById(trailer.getId()).get();
        // Disconnect from session so that the updates on updatedTrailer are not directly saved in db
        em.detach(updatedTrailer);
        updatedTrailer
            .plate(UPDATED_PLATE)
            .trailerType(UPDATED_TRAILER_TYPE)
            .brand(UPDATED_BRAND)
            .productionYear(UPDATED_PRODUCTION_YEAR)
            .technicalExamDate(UPDATED_TECHNICAL_EXAM_DATE)
            .supervisionExamDate(UPDATED_SUPERVISION_EXAM_DATE)
            .maxCapacity(UPDATED_MAX_CAPACITY);

        restTrailerMockMvc.perform(put("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrailer)))
            .andExpect(status().isOk());

        // Validate the Trailer in the database
        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeUpdate);
        Trailer testTrailer = trailerList.get(trailerList.size() - 1);
        assertThat(testTrailer.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testTrailer.getTrailerType()).isEqualTo(UPDATED_TRAILER_TYPE);
        assertThat(testTrailer.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testTrailer.getProductionYear()).isEqualTo(UPDATED_PRODUCTION_YEAR);
        assertThat(testTrailer.getTechnicalExamDate()).isEqualTo(UPDATED_TECHNICAL_EXAM_DATE);
        assertThat(testTrailer.getSupervisionExamDate()).isEqualTo(UPDATED_SUPERVISION_EXAM_DATE);
        assertThat(testTrailer.getMaxCapacity()).isEqualTo(UPDATED_MAX_CAPACITY);

        // Validate the Trailer in Elasticsearch
        verify(mockTrailerSearchRepository, times(1)).save(testTrailer);
    }

    @Test
    @Transactional
    public void updateNonExistingTrailer() throws Exception {
        int databaseSizeBeforeUpdate = trailerRepository.findAll().size();

        // Create the Trailer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrailerMockMvc.perform(put("/api/trailers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trailer)))
            .andExpect(status().isBadRequest());

        // Validate the Trailer in the database
        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Trailer in Elasticsearch
        verify(mockTrailerSearchRepository, times(0)).save(trailer);
    }

    @Test
    @Transactional
    public void deleteTrailer() throws Exception {
        // Initialize the database
        trailerService.save(trailer);

        int databaseSizeBeforeDelete = trailerRepository.findAll().size();

        // Get the trailer
        restTrailerMockMvc.perform(delete("/api/trailers/{id}", trailer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Trailer> trailerList = trailerRepository.findAll();
        assertThat(trailerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Trailer in Elasticsearch
        verify(mockTrailerSearchRepository, times(1)).deleteById(trailer.getId());
    }

    @Test
    @Transactional
    public void searchTrailer() throws Exception {
        // Initialize the database
        trailerService.save(trailer);
        when(mockTrailerSearchRepository.search(queryStringQuery("id:" + trailer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(trailer), PageRequest.of(0, 1), 1));
        // Search the trailer
        restTrailerMockMvc.perform(get("/api/_search/trailers?query=id:" + trailer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trailer.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
            .andExpect(jsonPath("$.[*].trailerType").value(hasItem(DEFAULT_TRAILER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].technicalExamDate").value(hasItem(DEFAULT_TECHNICAL_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].supervisionExamDate").value(hasItem(DEFAULT_SUPERVISION_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxCapacity").value(hasItem(DEFAULT_MAX_CAPACITY.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trailer.class);
        Trailer trailer1 = new Trailer();
        trailer1.setId(1L);
        Trailer trailer2 = new Trailer();
        trailer2.setId(trailer1.getId());
        assertThat(trailer1).isEqualTo(trailer2);
        trailer2.setId(2L);
        assertThat(trailer1).isNotEqualTo(trailer2);
        trailer1.setId(null);
        assertThat(trailer1).isNotEqualTo(trailer2);
    }
}
