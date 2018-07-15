package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.Distance;
import com.foxcode.adex.repository.DistanceRepository;
import com.foxcode.adex.repository.search.DistanceSearchRepository;
import com.foxcode.adex.service.DistanceService;
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

/**
 * Test class for the DistanceResource REST controller.
 *
 * @see DistanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class DistanceResourceIntTest {

    private static final String DEFAULT_START_CITY = "AAAAAAAAAA";
    private static final String UPDATED_START_CITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_START_KILOMETERS = 1L;
    private static final Long UPDATED_START_KILOMETERS = 2L;

    private static final String DEFAULT_END_CITY = "AAAAAAAAAA";
    private static final String UPDATED_END_CITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_END_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_END_KILOMETERS = 1L;
    private static final Long UPDATED_END_KILOMETERS = 2L;

    @Autowired
    private DistanceRepository distanceRepository;

    

    @Autowired
    private DistanceService distanceService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.DistanceSearchRepositoryMockConfiguration
     */
    @Autowired
    private DistanceSearchRepository mockDistanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDistanceMockMvc;

    private Distance distance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DistanceResource distanceResource = new DistanceResource(distanceService);
        this.restDistanceMockMvc = MockMvcBuilders.standaloneSetup(distanceResource)
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
    public static Distance createEntity(EntityManager em) {
        Distance distance = new Distance()
            .startCity(DEFAULT_START_CITY)
            .startDay(DEFAULT_START_DAY)
            .startKilometers(DEFAULT_START_KILOMETERS)
            .endCity(DEFAULT_END_CITY)
            .endDay(DEFAULT_END_DAY)
            .endKilometers(DEFAULT_END_KILOMETERS);
        return distance;
    }

    @Before
    public void initTest() {
        distance = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistance() throws Exception {
        int databaseSizeBeforeCreate = distanceRepository.findAll().size();

        // Create the Distance
        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isCreated());

        // Validate the Distance in the database
        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeCreate + 1);
        Distance testDistance = distanceList.get(distanceList.size() - 1);
        assertThat(testDistance.getStartCity()).isEqualTo(DEFAULT_START_CITY);
        assertThat(testDistance.getStartDay()).isEqualTo(DEFAULT_START_DAY);
        assertThat(testDistance.getStartKilometers()).isEqualTo(DEFAULT_START_KILOMETERS);
        assertThat(testDistance.getEndCity()).isEqualTo(DEFAULT_END_CITY);
        assertThat(testDistance.getEndDay()).isEqualTo(DEFAULT_END_DAY);
        assertThat(testDistance.getEndKilometers()).isEqualTo(DEFAULT_END_KILOMETERS);

        // Validate the Distance in Elasticsearch
        verify(mockDistanceSearchRepository, times(1)).save(testDistance);
    }

    @Test
    @Transactional
    public void createDistanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = distanceRepository.findAll().size();

        // Create the Distance with an existing ID
        distance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        // Validate the Distance in the database
        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Distance in Elasticsearch
        verify(mockDistanceSearchRepository, times(0)).save(distance);
    }

    @Test
    @Transactional
    public void checkStartCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = distanceRepository.findAll().size();
        // set the field null
        distance.setStartCity(null);

        // Create the Distance, which fails.

        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = distanceRepository.findAll().size();
        // set the field null
        distance.setStartDay(null);

        // Create the Distance, which fails.

        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartKilometersIsRequired() throws Exception {
        int databaseSizeBeforeTest = distanceRepository.findAll().size();
        // set the field null
        distance.setStartKilometers(null);

        // Create the Distance, which fails.

        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = distanceRepository.findAll().size();
        // set the field null
        distance.setEndCity(null);

        // Create the Distance, which fails.

        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = distanceRepository.findAll().size();
        // set the field null
        distance.setEndDay(null);

        // Create the Distance, which fails.

        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndKilometersIsRequired() throws Exception {
        int databaseSizeBeforeTest = distanceRepository.findAll().size();
        // set the field null
        distance.setEndKilometers(null);

        // Create the Distance, which fails.

        restDistanceMockMvc.perform(post("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDistances() throws Exception {
        // Initialize the database
        distanceRepository.saveAndFlush(distance);

        // Get all the distanceList
        restDistanceMockMvc.perform(get("/api/distances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distance.getId().intValue())))
            .andExpect(jsonPath("$.[*].startCity").value(hasItem(DEFAULT_START_CITY.toString())))
            .andExpect(jsonPath("$.[*].startDay").value(hasItem(DEFAULT_START_DAY.toString())))
            .andExpect(jsonPath("$.[*].startKilometers").value(hasItem(DEFAULT_START_KILOMETERS.intValue())))
            .andExpect(jsonPath("$.[*].endCity").value(hasItem(DEFAULT_END_CITY.toString())))
            .andExpect(jsonPath("$.[*].endDay").value(hasItem(DEFAULT_END_DAY.toString())))
            .andExpect(jsonPath("$.[*].endKilometers").value(hasItem(DEFAULT_END_KILOMETERS.intValue())));
    }
    

    @Test
    @Transactional
    public void getDistance() throws Exception {
        // Initialize the database
        distanceRepository.saveAndFlush(distance);

        // Get the distance
        restDistanceMockMvc.perform(get("/api/distances/{id}", distance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(distance.getId().intValue()))
            .andExpect(jsonPath("$.startCity").value(DEFAULT_START_CITY.toString()))
            .andExpect(jsonPath("$.startDay").value(DEFAULT_START_DAY.toString()))
            .andExpect(jsonPath("$.startKilometers").value(DEFAULT_START_KILOMETERS.intValue()))
            .andExpect(jsonPath("$.endCity").value(DEFAULT_END_CITY.toString()))
            .andExpect(jsonPath("$.endDay").value(DEFAULT_END_DAY.toString()))
            .andExpect(jsonPath("$.endKilometers").value(DEFAULT_END_KILOMETERS.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingDistance() throws Exception {
        // Get the distance
        restDistanceMockMvc.perform(get("/api/distances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistance() throws Exception {
        // Initialize the database
        distanceService.save(distance);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDistanceSearchRepository);

        int databaseSizeBeforeUpdate = distanceRepository.findAll().size();

        // Update the distance
        Distance updatedDistance = distanceRepository.findById(distance.getId()).get();
        // Disconnect from session so that the updates on updatedDistance are not directly saved in db
        em.detach(updatedDistance);
        updatedDistance
            .startCity(UPDATED_START_CITY)
            .startDay(UPDATED_START_DAY)
            .startKilometers(UPDATED_START_KILOMETERS)
            .endCity(UPDATED_END_CITY)
            .endDay(UPDATED_END_DAY)
            .endKilometers(UPDATED_END_KILOMETERS);

        restDistanceMockMvc.perform(put("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDistance)))
            .andExpect(status().isOk());

        // Validate the Distance in the database
        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeUpdate);
        Distance testDistance = distanceList.get(distanceList.size() - 1);
        assertThat(testDistance.getStartCity()).isEqualTo(UPDATED_START_CITY);
        assertThat(testDistance.getStartDay()).isEqualTo(UPDATED_START_DAY);
        assertThat(testDistance.getStartKilometers()).isEqualTo(UPDATED_START_KILOMETERS);
        assertThat(testDistance.getEndCity()).isEqualTo(UPDATED_END_CITY);
        assertThat(testDistance.getEndDay()).isEqualTo(UPDATED_END_DAY);
        assertThat(testDistance.getEndKilometers()).isEqualTo(UPDATED_END_KILOMETERS);

        // Validate the Distance in Elasticsearch
        verify(mockDistanceSearchRepository, times(1)).save(testDistance);
    }

    @Test
    @Transactional
    public void updateNonExistingDistance() throws Exception {
        int databaseSizeBeforeUpdate = distanceRepository.findAll().size();

        // Create the Distance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDistanceMockMvc.perform(put("/api/distances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distance)))
            .andExpect(status().isBadRequest());

        // Validate the Distance in the database
        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Distance in Elasticsearch
        verify(mockDistanceSearchRepository, times(0)).save(distance);
    }

    @Test
    @Transactional
    public void deleteDistance() throws Exception {
        // Initialize the database
        distanceService.save(distance);

        int databaseSizeBeforeDelete = distanceRepository.findAll().size();

        // Get the distance
        restDistanceMockMvc.perform(delete("/api/distances/{id}", distance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Distance> distanceList = distanceRepository.findAll();
        assertThat(distanceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Distance in Elasticsearch
        verify(mockDistanceSearchRepository, times(1)).deleteById(distance.getId());
    }

    @Test
    @Transactional
    public void searchDistance() throws Exception {
        // Initialize the database
        distanceService.save(distance);
        when(mockDistanceSearchRepository.search(queryStringQuery("id:" + distance.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(distance), PageRequest.of(0, 1), 1));
        // Search the distance
        restDistanceMockMvc.perform(get("/api/_search/distances?query=id:" + distance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distance.getId().intValue())))
            .andExpect(jsonPath("$.[*].startCity").value(hasItem(DEFAULT_START_CITY.toString())))
            .andExpect(jsonPath("$.[*].startDay").value(hasItem(DEFAULT_START_DAY.toString())))
            .andExpect(jsonPath("$.[*].startKilometers").value(hasItem(DEFAULT_START_KILOMETERS.intValue())))
            .andExpect(jsonPath("$.[*].endCity").value(hasItem(DEFAULT_END_CITY.toString())))
            .andExpect(jsonPath("$.[*].endDay").value(hasItem(DEFAULT_END_DAY.toString())))
            .andExpect(jsonPath("$.[*].endKilometers").value(hasItem(DEFAULT_END_KILOMETERS.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distance.class);
        Distance distance1 = new Distance();
        distance1.setId(1L);
        Distance distance2 = new Distance();
        distance2.setId(distance1.getId());
        assertThat(distance1).isEqualTo(distance2);
        distance2.setId(2L);
        assertThat(distance1).isNotEqualTo(distance2);
        distance1.setId(null);
        assertThat(distance1).isNotEqualTo(distance2);
    }
}
