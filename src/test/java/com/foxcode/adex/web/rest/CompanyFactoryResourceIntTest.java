package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.CompanyFactory;
import com.foxcode.adex.repository.CompanyFactoryRepository;
import com.foxcode.adex.repository.search.CompanyFactorySearchRepository;
import com.foxcode.adex.service.CompanyFactoryService;
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
 * Test class for the CompanyFactoryResource REST controller.
 *
 * @see CompanyFactoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class CompanyFactoryResourceIntTest {

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOUSE_NUMBER = 1;
    private static final Integer UPDATED_HOUSE_NUMBER = 2;

    private static final Integer DEFAULT_FLAT_NUMBER = 1;
    private static final Integer UPDATED_FLAT_NUMBER = 2;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private CompanyFactoryRepository companyFactoryRepository;

    

    @Autowired
    private CompanyFactoryService companyFactoryService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.CompanyFactorySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanyFactorySearchRepository mockCompanyFactorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyFactoryMockMvc;

    private CompanyFactory companyFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyFactoryResource companyFactoryResource = new CompanyFactoryResource(companyFactoryService);
        this.restCompanyFactoryMockMvc = MockMvcBuilders.standaloneSetup(companyFactoryResource)
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
    public static CompanyFactory createEntity(EntityManager em) {
        CompanyFactory companyFactory = new CompanyFactory()
            .city(DEFAULT_CITY)
            .postCode(DEFAULT_POST_CODE)
            .street(DEFAULT_STREET)
            .houseNumber(DEFAULT_HOUSE_NUMBER)
            .flatNumber(DEFAULT_FLAT_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return companyFactory;
    }

    @Before
    public void initTest() {
        companyFactory = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyFactory() throws Exception {
        int databaseSizeBeforeCreate = companyFactoryRepository.findAll().size();

        // Create the CompanyFactory
        restCompanyFactoryMockMvc.perform(post("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isCreated());

        // Validate the CompanyFactory in the database
        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyFactory testCompanyFactory = companyFactoryList.get(companyFactoryList.size() - 1);
        assertThat(testCompanyFactory.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCompanyFactory.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testCompanyFactory.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testCompanyFactory.getHouseNumber()).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testCompanyFactory.getFlatNumber()).isEqualTo(DEFAULT_FLAT_NUMBER);
        assertThat(testCompanyFactory.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);

        // Validate the CompanyFactory in Elasticsearch
        verify(mockCompanyFactorySearchRepository, times(1)).save(testCompanyFactory);
    }

    @Test
    @Transactional
    public void createCompanyFactoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyFactoryRepository.findAll().size();

        // Create the CompanyFactory with an existing ID
        companyFactory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyFactoryMockMvc.perform(post("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyFactory in the database
        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the CompanyFactory in Elasticsearch
        verify(mockCompanyFactorySearchRepository, times(0)).save(companyFactory);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyFactoryRepository.findAll().size();
        // set the field null
        companyFactory.setCity(null);

        // Create the CompanyFactory, which fails.

        restCompanyFactoryMockMvc.perform(post("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isBadRequest());

        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyFactoryRepository.findAll().size();
        // set the field null
        companyFactory.setPostCode(null);

        // Create the CompanyFactory, which fails.

        restCompanyFactoryMockMvc.perform(post("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isBadRequest());

        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyFactoryRepository.findAll().size();
        // set the field null
        companyFactory.setStreet(null);

        // Create the CompanyFactory, which fails.

        restCompanyFactoryMockMvc.perform(post("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isBadRequest());

        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHouseNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyFactoryRepository.findAll().size();
        // set the field null
        companyFactory.setHouseNumber(null);

        // Create the CompanyFactory, which fails.

        restCompanyFactoryMockMvc.perform(post("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isBadRequest());

        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyFactories() throws Exception {
        // Initialize the database
        companyFactoryRepository.saveAndFlush(companyFactory);

        // Get all the companyFactoryList
        restCompanyFactoryMockMvc.perform(get("/api/company-factories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyFactory.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].flatNumber").value(hasItem(DEFAULT_FLAT_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }
    

    @Test
    @Transactional
    public void getCompanyFactory() throws Exception {
        // Initialize the database
        companyFactoryRepository.saveAndFlush(companyFactory);

        // Get the companyFactory
        restCompanyFactoryMockMvc.perform(get("/api/company-factories/{id}", companyFactory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyFactory.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.houseNumber").value(DEFAULT_HOUSE_NUMBER))
            .andExpect(jsonPath("$.flatNumber").value(DEFAULT_FLAT_NUMBER))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCompanyFactory() throws Exception {
        // Get the companyFactory
        restCompanyFactoryMockMvc.perform(get("/api/company-factories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyFactory() throws Exception {
        // Initialize the database
        companyFactoryService.save(companyFactory);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCompanyFactorySearchRepository);

        int databaseSizeBeforeUpdate = companyFactoryRepository.findAll().size();

        // Update the companyFactory
        CompanyFactory updatedCompanyFactory = companyFactoryRepository.findById(companyFactory.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyFactory are not directly saved in db
        em.detach(updatedCompanyFactory);
        updatedCompanyFactory
            .city(UPDATED_CITY)
            .postCode(UPDATED_POST_CODE)
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .flatNumber(UPDATED_FLAT_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCompanyFactoryMockMvc.perform(put("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyFactory)))
            .andExpect(status().isOk());

        // Validate the CompanyFactory in the database
        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeUpdate);
        CompanyFactory testCompanyFactory = companyFactoryList.get(companyFactoryList.size() - 1);
        assertThat(testCompanyFactory.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompanyFactory.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCompanyFactory.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testCompanyFactory.getHouseNumber()).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testCompanyFactory.getFlatNumber()).isEqualTo(UPDATED_FLAT_NUMBER);
        assertThat(testCompanyFactory.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);

        // Validate the CompanyFactory in Elasticsearch
        verify(mockCompanyFactorySearchRepository, times(1)).save(testCompanyFactory);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyFactory() throws Exception {
        int databaseSizeBeforeUpdate = companyFactoryRepository.findAll().size();

        // Create the CompanyFactory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyFactoryMockMvc.perform(put("/api/company-factories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyFactory)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyFactory in the database
        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CompanyFactory in Elasticsearch
        verify(mockCompanyFactorySearchRepository, times(0)).save(companyFactory);
    }

    @Test
    @Transactional
    public void deleteCompanyFactory() throws Exception {
        // Initialize the database
        companyFactoryService.save(companyFactory);

        int databaseSizeBeforeDelete = companyFactoryRepository.findAll().size();

        // Get the companyFactory
        restCompanyFactoryMockMvc.perform(delete("/api/company-factories/{id}", companyFactory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyFactory> companyFactoryList = companyFactoryRepository.findAll();
        assertThat(companyFactoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CompanyFactory in Elasticsearch
        verify(mockCompanyFactorySearchRepository, times(1)).deleteById(companyFactory.getId());
    }

    @Test
    @Transactional
    public void searchCompanyFactory() throws Exception {
        // Initialize the database
        companyFactoryService.save(companyFactory);
        when(mockCompanyFactorySearchRepository.search(queryStringQuery("id:" + companyFactory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(companyFactory), PageRequest.of(0, 1), 1));
        // Search the companyFactory
        restCompanyFactoryMockMvc.perform(get("/api/_search/company-factories?query=id:" + companyFactory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyFactory.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].flatNumber").value(hasItem(DEFAULT_FLAT_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyFactory.class);
        CompanyFactory companyFactory1 = new CompanyFactory();
        companyFactory1.setId(1L);
        CompanyFactory companyFactory2 = new CompanyFactory();
        companyFactory2.setId(companyFactory1.getId());
        assertThat(companyFactory1).isEqualTo(companyFactory2);
        companyFactory2.setId(2L);
        assertThat(companyFactory1).isNotEqualTo(companyFactory2);
        companyFactory1.setId(null);
        assertThat(companyFactory1).isNotEqualTo(companyFactory2);
    }
}
