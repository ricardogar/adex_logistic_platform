package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.CompanyMain;
import com.foxcode.adex.repository.CompanyMainRepository;
import com.foxcode.adex.repository.search.CompanyMainSearchRepository;
import com.foxcode.adex.service.CompanyMainService;
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
 * Test class for the CompanyMainResource REST controller.
 *
 * @see CompanyMainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class CompanyMainResourceIntTest {

    private static final String DEFAULT_NIP = "AAAAAAAAAA";
    private static final String UPDATED_NIP = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private CompanyMainRepository companyMainRepository;

    

    @Autowired
    private CompanyMainService companyMainService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.CompanyMainSearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanyMainSearchRepository mockCompanyMainSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyMainMockMvc;

    private CompanyMain companyMain;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyMainResource companyMainResource = new CompanyMainResource(companyMainService);
        this.restCompanyMainMockMvc = MockMvcBuilders.standaloneSetup(companyMainResource)
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
    public static CompanyMain createEntity(EntityManager em) {
        CompanyMain companyMain = new CompanyMain()
            .nip(DEFAULT_NIP)
            .name(DEFAULT_NAME)
            .city(DEFAULT_CITY)
            .postCode(DEFAULT_POST_CODE)
            .street(DEFAULT_STREET)
            .houseNumber(DEFAULT_HOUSE_NUMBER)
            .flatNumber(DEFAULT_FLAT_NUMBER)
            .email(DEFAULT_EMAIL);
        return companyMain;
    }

    @Before
    public void initTest() {
        companyMain = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyMain() throws Exception {
        int databaseSizeBeforeCreate = companyMainRepository.findAll().size();

        // Create the CompanyMain
        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isCreated());

        // Validate the CompanyMain in the database
        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyMain testCompanyMain = companyMainList.get(companyMainList.size() - 1);
        assertThat(testCompanyMain.getNip()).isEqualTo(DEFAULT_NIP);
        assertThat(testCompanyMain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanyMain.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCompanyMain.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testCompanyMain.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testCompanyMain.getHouseNumber()).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testCompanyMain.getFlatNumber()).isEqualTo(DEFAULT_FLAT_NUMBER);
        assertThat(testCompanyMain.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the CompanyMain in Elasticsearch
        verify(mockCompanyMainSearchRepository, times(1)).save(testCompanyMain);
    }

    @Test
    @Transactional
    public void createCompanyMainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyMainRepository.findAll().size();

        // Create the CompanyMain with an existing ID
        companyMain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyMain in the database
        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeCreate);

        // Validate the CompanyMain in Elasticsearch
        verify(mockCompanyMainSearchRepository, times(0)).save(companyMain);
    }

    @Test
    @Transactional
    public void checkNipIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyMainRepository.findAll().size();
        // set the field null
        companyMain.setNip(null);

        // Create the CompanyMain, which fails.

        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyMainRepository.findAll().size();
        // set the field null
        companyMain.setName(null);

        // Create the CompanyMain, which fails.

        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyMainRepository.findAll().size();
        // set the field null
        companyMain.setCity(null);

        // Create the CompanyMain, which fails.

        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyMainRepository.findAll().size();
        // set the field null
        companyMain.setPostCode(null);

        // Create the CompanyMain, which fails.

        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyMainRepository.findAll().size();
        // set the field null
        companyMain.setStreet(null);

        // Create the CompanyMain, which fails.

        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHouseNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyMainRepository.findAll().size();
        // set the field null
        companyMain.setHouseNumber(null);

        // Create the CompanyMain, which fails.

        restCompanyMainMockMvc.perform(post("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyMains() throws Exception {
        // Initialize the database
        companyMainRepository.saveAndFlush(companyMain);

        // Get all the companyMainList
        restCompanyMainMockMvc.perform(get("/api/company-mains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyMain.getId().intValue())))
            .andExpect(jsonPath("$.[*].nip").value(hasItem(DEFAULT_NIP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].flatNumber").value(hasItem(DEFAULT_FLAT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    

    @Test
    @Transactional
    public void getCompanyMain() throws Exception {
        // Initialize the database
        companyMainRepository.saveAndFlush(companyMain);

        // Get the companyMain
        restCompanyMainMockMvc.perform(get("/api/company-mains/{id}", companyMain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyMain.getId().intValue()))
            .andExpect(jsonPath("$.nip").value(DEFAULT_NIP.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.houseNumber").value(DEFAULT_HOUSE_NUMBER))
            .andExpect(jsonPath("$.flatNumber").value(DEFAULT_FLAT_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCompanyMain() throws Exception {
        // Get the companyMain
        restCompanyMainMockMvc.perform(get("/api/company-mains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyMain() throws Exception {
        // Initialize the database
        companyMainService.save(companyMain);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCompanyMainSearchRepository);

        int databaseSizeBeforeUpdate = companyMainRepository.findAll().size();

        // Update the companyMain
        CompanyMain updatedCompanyMain = companyMainRepository.findById(companyMain.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyMain are not directly saved in db
        em.detach(updatedCompanyMain);
        updatedCompanyMain
            .nip(UPDATED_NIP)
            .name(UPDATED_NAME)
            .city(UPDATED_CITY)
            .postCode(UPDATED_POST_CODE)
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .flatNumber(UPDATED_FLAT_NUMBER)
            .email(UPDATED_EMAIL);

        restCompanyMainMockMvc.perform(put("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyMain)))
            .andExpect(status().isOk());

        // Validate the CompanyMain in the database
        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeUpdate);
        CompanyMain testCompanyMain = companyMainList.get(companyMainList.size() - 1);
        assertThat(testCompanyMain.getNip()).isEqualTo(UPDATED_NIP);
        assertThat(testCompanyMain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanyMain.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompanyMain.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCompanyMain.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testCompanyMain.getHouseNumber()).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testCompanyMain.getFlatNumber()).isEqualTo(UPDATED_FLAT_NUMBER);
        assertThat(testCompanyMain.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the CompanyMain in Elasticsearch
        verify(mockCompanyMainSearchRepository, times(1)).save(testCompanyMain);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyMain() throws Exception {
        int databaseSizeBeforeUpdate = companyMainRepository.findAll().size();

        // Create the CompanyMain

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyMainMockMvc.perform(put("/api/company-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyMain)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyMain in the database
        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CompanyMain in Elasticsearch
        verify(mockCompanyMainSearchRepository, times(0)).save(companyMain);
    }

    @Test
    @Transactional
    public void deleteCompanyMain() throws Exception {
        // Initialize the database
        companyMainService.save(companyMain);

        int databaseSizeBeforeDelete = companyMainRepository.findAll().size();

        // Get the companyMain
        restCompanyMainMockMvc.perform(delete("/api/company-mains/{id}", companyMain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyMain> companyMainList = companyMainRepository.findAll();
        assertThat(companyMainList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CompanyMain in Elasticsearch
        verify(mockCompanyMainSearchRepository, times(1)).deleteById(companyMain.getId());
    }

    @Test
    @Transactional
    public void searchCompanyMain() throws Exception {
        // Initialize the database
        companyMainService.save(companyMain);
        when(mockCompanyMainSearchRepository.search(queryStringQuery("id:" + companyMain.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(companyMain), PageRequest.of(0, 1), 1));
        // Search the companyMain
        restCompanyMainMockMvc.perform(get("/api/_search/company-mains?query=id:" + companyMain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyMain.getId().intValue())))
            .andExpect(jsonPath("$.[*].nip").value(hasItem(DEFAULT_NIP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].flatNumber").value(hasItem(DEFAULT_FLAT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyMain.class);
        CompanyMain companyMain1 = new CompanyMain();
        companyMain1.setId(1L);
        CompanyMain companyMain2 = new CompanyMain();
        companyMain2.setId(companyMain1.getId());
        assertThat(companyMain1).isEqualTo(companyMain2);
        companyMain2.setId(2L);
        assertThat(companyMain1).isNotEqualTo(companyMain2);
        companyMain1.setId(null);
        assertThat(companyMain1).isNotEqualTo(companyMain2);
    }
}
