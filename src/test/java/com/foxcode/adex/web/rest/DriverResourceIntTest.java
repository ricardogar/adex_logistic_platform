package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.Driver;
import com.foxcode.adex.repository.DriverRepository;
import com.foxcode.adex.repository.search.DriverSearchRepository;
import com.foxcode.adex.service.DriverService;
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
 * Test class for the DriverResource REST controller.
 *
 * @see DriverResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class DriverResourceIntTest {

    private static final String DEFAULT_PESEL = "AAAAAAAAAA";
    private static final String UPDATED_PESEL = "BBBBBBBBBB";

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

    private static final LocalDate DEFAULT_MEDICAL_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEDICAL_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DRIVER_LICENCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DRIVER_LICENCE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DriverRepository driverRepository;

    

    @Autowired
    private DriverService driverService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.DriverSearchRepositoryMockConfiguration
     */
    @Autowired
    private DriverSearchRepository mockDriverSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDriverMockMvc;

    private Driver driver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DriverResource driverResource = new DriverResource(driverService);
        this.restDriverMockMvc = MockMvcBuilders.standaloneSetup(driverResource)
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
    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver()
            .pesel(DEFAULT_PESEL)
            .city(DEFAULT_CITY)
            .postCode(DEFAULT_POST_CODE)
            .street(DEFAULT_STREET)
            .houseNumber(DEFAULT_HOUSE_NUMBER)
            .flatNumber(DEFAULT_FLAT_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .medicalExamDate(DEFAULT_MEDICAL_EXAM_DATE)
            .driverLicenceDate(DEFAULT_DRIVER_LICENCE_DATE);
        return driver;
    }

    @Before
    public void initTest() {
        driver = createEntity(em);
    }

    @Test
    @Transactional
    public void createDriver() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().size();

        // Create the Driver
        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isCreated());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate + 1);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getPesel()).isEqualTo(DEFAULT_PESEL);
        assertThat(testDriver.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testDriver.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testDriver.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testDriver.getHouseNumber()).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testDriver.getFlatNumber()).isEqualTo(DEFAULT_FLAT_NUMBER);
        assertThat(testDriver.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testDriver.getMedicalExamDate()).isEqualTo(DEFAULT_MEDICAL_EXAM_DATE);
        assertThat(testDriver.getDriverLicenceDate()).isEqualTo(DEFAULT_DRIVER_LICENCE_DATE);

        // Validate the Driver in Elasticsearch
        verify(mockDriverSearchRepository, times(1)).save(testDriver);
    }

    @Test
    @Transactional
    public void createDriverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().size();

        // Create the Driver with an existing ID
        driver.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate);

        // Validate the Driver in Elasticsearch
        verify(mockDriverSearchRepository, times(0)).save(driver);
    }

    @Test
    @Transactional
    public void checkPeselIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setPesel(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setCity(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setPostCode(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setStreet(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHouseNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setHouseNumber(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setPhoneNumber(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMedicalExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setMedicalExamDate(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDriverLicenceDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setDriverLicenceDate(null);

        // Create the Driver, which fails.

        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDrivers() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList
        restDriverMockMvc.perform(get("/api/drivers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].pesel").value(hasItem(DEFAULT_PESEL.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].flatNumber").value(hasItem(DEFAULT_FLAT_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].medicalExamDate").value(hasItem(DEFAULT_MEDICAL_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].driverLicenceDate").value(hasItem(DEFAULT_DRIVER_LICENCE_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get the driver
        restDriverMockMvc.perform(get("/api/drivers/{id}", driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(driver.getId().intValue()))
            .andExpect(jsonPath("$.pesel").value(DEFAULT_PESEL.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.houseNumber").value(DEFAULT_HOUSE_NUMBER))
            .andExpect(jsonPath("$.flatNumber").value(DEFAULT_FLAT_NUMBER))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.medicalExamDate").value(DEFAULT_MEDICAL_EXAM_DATE.toString()))
            .andExpect(jsonPath("$.driverLicenceDate").value(DEFAULT_DRIVER_LICENCE_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDriver() throws Exception {
        // Get the driver
        restDriverMockMvc.perform(get("/api/drivers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDriver() throws Exception {
        // Initialize the database
        driverService.save(driver);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDriverSearchRepository);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).get();
        // Disconnect from session so that the updates on updatedDriver are not directly saved in db
        em.detach(updatedDriver);
        updatedDriver
            .pesel(UPDATED_PESEL)
            .city(UPDATED_CITY)
            .postCode(UPDATED_POST_CODE)
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .flatNumber(UPDATED_FLAT_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .medicalExamDate(UPDATED_MEDICAL_EXAM_DATE)
            .driverLicenceDate(UPDATED_DRIVER_LICENCE_DATE);

        restDriverMockMvc.perform(put("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDriver)))
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getPesel()).isEqualTo(UPDATED_PESEL);
        assertThat(testDriver.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testDriver.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testDriver.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testDriver.getHouseNumber()).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testDriver.getFlatNumber()).isEqualTo(UPDATED_FLAT_NUMBER);
        assertThat(testDriver.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testDriver.getMedicalExamDate()).isEqualTo(UPDATED_MEDICAL_EXAM_DATE);
        assertThat(testDriver.getDriverLicenceDate()).isEqualTo(UPDATED_DRIVER_LICENCE_DATE);

        // Validate the Driver in Elasticsearch
        verify(mockDriverSearchRepository, times(1)).save(testDriver);
    }

    @Test
    @Transactional
    public void updateNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Create the Driver

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDriverMockMvc.perform(put("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driver)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Driver in Elasticsearch
        verify(mockDriverSearchRepository, times(0)).save(driver);
    }

    @Test
    @Transactional
    public void deleteDriver() throws Exception {
        // Initialize the database
        driverService.save(driver);

        int databaseSizeBeforeDelete = driverRepository.findAll().size();

        // Get the driver
        restDriverMockMvc.perform(delete("/api/drivers/{id}", driver.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Driver in Elasticsearch
        verify(mockDriverSearchRepository, times(1)).deleteById(driver.getId());
    }

    @Test
    @Transactional
    public void searchDriver() throws Exception {
        // Initialize the database
        driverService.save(driver);
        when(mockDriverSearchRepository.search(queryStringQuery("id:" + driver.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(driver), PageRequest.of(0, 1), 1));
        // Search the driver
        restDriverMockMvc.perform(get("/api/_search/drivers?query=id:" + driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].pesel").value(hasItem(DEFAULT_PESEL.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].flatNumber").value(hasItem(DEFAULT_FLAT_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].medicalExamDate").value(hasItem(DEFAULT_MEDICAL_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].driverLicenceDate").value(hasItem(DEFAULT_DRIVER_LICENCE_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Driver.class);
        Driver driver1 = new Driver();
        driver1.setId(1L);
        Driver driver2 = new Driver();
        driver2.setId(driver1.getId());
        assertThat(driver1).isEqualTo(driver2);
        driver2.setId(2L);
        assertThat(driver1).isNotEqualTo(driver2);
        driver1.setId(null);
        assertThat(driver1).isNotEqualTo(driver2);
    }
}
