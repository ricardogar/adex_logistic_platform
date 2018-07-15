package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.Truck;
import com.foxcode.adex.repository.TruckRepository;
import com.foxcode.adex.repository.search.TruckSearchRepository;
import com.foxcode.adex.service.TruckService;
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

import com.foxcode.adex.domain.enumeration.EmissionStandard;
/**
 * Test class for the TruckResource REST controller.
 *
 * @see TruckResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class TruckResourceIntTest {

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCTION_YEAR = 1;
    private static final Integer UPDATED_PRODUCTION_YEAR = 2;

    private static final EmissionStandard DEFAULT_EMISSION_STANDARD = EmissionStandard.EURO_3;
    private static final EmissionStandard UPDATED_EMISSION_STANDARD = EmissionStandard.EURO_4;

    private static final Integer DEFAULT_HORSE_POWER = 1;
    private static final Integer UPDATED_HORSE_POWER = 2;

    private static final Integer DEFAULT_FUEL_TANK = 1;
    private static final Integer UPDATED_FUEL_TANK = 2;

    private static final LocalDate DEFAULT_TECHNICAL_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TECHNICAL_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_COMPRESSOR = false;
    private static final Boolean UPDATED_COMPRESSOR = true;

    private static final Boolean DEFAULT_HYDRAULICS = false;
    private static final Boolean UPDATED_HYDRAULICS = true;

    private static final Boolean DEFAULT_GPS = false;
    private static final Boolean UPDATED_GPS = true;

    private static final Boolean DEFAULT_INTERNATIONAL_LICENSE = false;
    private static final Boolean UPDATED_INTERNATIONAL_LICENSE = true;

    @Autowired
    private TruckRepository truckRepository;

    

    @Autowired
    private TruckService truckService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.TruckSearchRepositoryMockConfiguration
     */
    @Autowired
    private TruckSearchRepository mockTruckSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTruckMockMvc;

    private Truck truck;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TruckResource truckResource = new TruckResource(truckService);
        this.restTruckMockMvc = MockMvcBuilders.standaloneSetup(truckResource)
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
    public static Truck createEntity(EntityManager em) {
        Truck truck = new Truck()
            .plate(DEFAULT_PLATE)
            .brand(DEFAULT_BRAND)
            .productionYear(DEFAULT_PRODUCTION_YEAR)
            .emissionStandard(DEFAULT_EMISSION_STANDARD)
            .horsePower(DEFAULT_HORSE_POWER)
            .fuelTank(DEFAULT_FUEL_TANK)
            .technicalExamDate(DEFAULT_TECHNICAL_EXAM_DATE)
            .compressor(DEFAULT_COMPRESSOR)
            .hydraulics(DEFAULT_HYDRAULICS)
            .gps(DEFAULT_GPS)
            .internationalLicense(DEFAULT_INTERNATIONAL_LICENSE);
        return truck;
    }

    @Before
    public void initTest() {
        truck = createEntity(em);
    }

    @Test
    @Transactional
    public void createTruck() throws Exception {
        int databaseSizeBeforeCreate = truckRepository.findAll().size();

        // Create the Truck
        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isCreated());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeCreate + 1);
        Truck testTruck = truckList.get(truckList.size() - 1);
        assertThat(testTruck.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testTruck.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testTruck.getProductionYear()).isEqualTo(DEFAULT_PRODUCTION_YEAR);
        assertThat(testTruck.getEmissionStandard()).isEqualTo(DEFAULT_EMISSION_STANDARD);
        assertThat(testTruck.getHorsePower()).isEqualTo(DEFAULT_HORSE_POWER);
        assertThat(testTruck.getFuelTank()).isEqualTo(DEFAULT_FUEL_TANK);
        assertThat(testTruck.getTechnicalExamDate()).isEqualTo(DEFAULT_TECHNICAL_EXAM_DATE);
        assertThat(testTruck.isCompressor()).isEqualTo(DEFAULT_COMPRESSOR);
        assertThat(testTruck.isHydraulics()).isEqualTo(DEFAULT_HYDRAULICS);
        assertThat(testTruck.isGps()).isEqualTo(DEFAULT_GPS);
        assertThat(testTruck.isInternationalLicense()).isEqualTo(DEFAULT_INTERNATIONAL_LICENSE);

        // Validate the Truck in Elasticsearch
        verify(mockTruckSearchRepository, times(1)).save(testTruck);
    }

    @Test
    @Transactional
    public void createTruckWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = truckRepository.findAll().size();

        // Create the Truck with an existing ID
        truck.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeCreate);

        // Validate the Truck in Elasticsearch
        verify(mockTruckSearchRepository, times(0)).save(truck);
    }

    @Test
    @Transactional
    public void checkPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setPlate(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setBrand(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductionYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setProductionYear(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmissionStandardIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setEmissionStandard(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHorsePowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setHorsePower(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFuelTankIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setFuelTank(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTechnicalExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setTechnicalExamDate(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompressorIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setCompressor(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHydraulicsIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setHydraulics(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGpsIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setGps(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInternationalLicenseIsRequired() throws Exception {
        int databaseSizeBeforeTest = truckRepository.findAll().size();
        // set the field null
        truck.setInternationalLicense(null);

        // Create the Truck, which fails.

        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrucks() throws Exception {
        // Initialize the database
        truckRepository.saveAndFlush(truck);

        // Get all the truckList
        restTruckMockMvc.perform(get("/api/trucks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truck.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].emissionStandard").value(hasItem(DEFAULT_EMISSION_STANDARD.toString())))
            .andExpect(jsonPath("$.[*].horsePower").value(hasItem(DEFAULT_HORSE_POWER)))
            .andExpect(jsonPath("$.[*].fuelTank").value(hasItem(DEFAULT_FUEL_TANK)))
            .andExpect(jsonPath("$.[*].technicalExamDate").value(hasItem(DEFAULT_TECHNICAL_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].compressor").value(hasItem(DEFAULT_COMPRESSOR.booleanValue())))
            .andExpect(jsonPath("$.[*].hydraulics").value(hasItem(DEFAULT_HYDRAULICS.booleanValue())))
            .andExpect(jsonPath("$.[*].gps").value(hasItem(DEFAULT_GPS.booleanValue())))
            .andExpect(jsonPath("$.[*].internationalLicense").value(hasItem(DEFAULT_INTERNATIONAL_LICENSE.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getTruck() throws Exception {
        // Initialize the database
        truckRepository.saveAndFlush(truck);

        // Get the truck
        restTruckMockMvc.perform(get("/api/trucks/{id}", truck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(truck.getId().intValue()))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.productionYear").value(DEFAULT_PRODUCTION_YEAR))
            .andExpect(jsonPath("$.emissionStandard").value(DEFAULT_EMISSION_STANDARD.toString()))
            .andExpect(jsonPath("$.horsePower").value(DEFAULT_HORSE_POWER))
            .andExpect(jsonPath("$.fuelTank").value(DEFAULT_FUEL_TANK))
            .andExpect(jsonPath("$.technicalExamDate").value(DEFAULT_TECHNICAL_EXAM_DATE.toString()))
            .andExpect(jsonPath("$.compressor").value(DEFAULT_COMPRESSOR.booleanValue()))
            .andExpect(jsonPath("$.hydraulics").value(DEFAULT_HYDRAULICS.booleanValue()))
            .andExpect(jsonPath("$.gps").value(DEFAULT_GPS.booleanValue()))
            .andExpect(jsonPath("$.internationalLicense").value(DEFAULT_INTERNATIONAL_LICENSE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTruck() throws Exception {
        // Get the truck
        restTruckMockMvc.perform(get("/api/trucks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTruck() throws Exception {
        // Initialize the database
        truckService.save(truck);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTruckSearchRepository);

        int databaseSizeBeforeUpdate = truckRepository.findAll().size();

        // Update the truck
        Truck updatedTruck = truckRepository.findById(truck.getId()).get();
        // Disconnect from session so that the updates on updatedTruck are not directly saved in db
        em.detach(updatedTruck);
        updatedTruck
            .plate(UPDATED_PLATE)
            .brand(UPDATED_BRAND)
            .productionYear(UPDATED_PRODUCTION_YEAR)
            .emissionStandard(UPDATED_EMISSION_STANDARD)
            .horsePower(UPDATED_HORSE_POWER)
            .fuelTank(UPDATED_FUEL_TANK)
            .technicalExamDate(UPDATED_TECHNICAL_EXAM_DATE)
            .compressor(UPDATED_COMPRESSOR)
            .hydraulics(UPDATED_HYDRAULICS)
            .gps(UPDATED_GPS)
            .internationalLicense(UPDATED_INTERNATIONAL_LICENSE);

        restTruckMockMvc.perform(put("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTruck)))
            .andExpect(status().isOk());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeUpdate);
        Truck testTruck = truckList.get(truckList.size() - 1);
        assertThat(testTruck.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testTruck.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testTruck.getProductionYear()).isEqualTo(UPDATED_PRODUCTION_YEAR);
        assertThat(testTruck.getEmissionStandard()).isEqualTo(UPDATED_EMISSION_STANDARD);
        assertThat(testTruck.getHorsePower()).isEqualTo(UPDATED_HORSE_POWER);
        assertThat(testTruck.getFuelTank()).isEqualTo(UPDATED_FUEL_TANK);
        assertThat(testTruck.getTechnicalExamDate()).isEqualTo(UPDATED_TECHNICAL_EXAM_DATE);
        assertThat(testTruck.isCompressor()).isEqualTo(UPDATED_COMPRESSOR);
        assertThat(testTruck.isHydraulics()).isEqualTo(UPDATED_HYDRAULICS);
        assertThat(testTruck.isGps()).isEqualTo(UPDATED_GPS);
        assertThat(testTruck.isInternationalLicense()).isEqualTo(UPDATED_INTERNATIONAL_LICENSE);

        // Validate the Truck in Elasticsearch
        verify(mockTruckSearchRepository, times(1)).save(testTruck);
    }

    @Test
    @Transactional
    public void updateNonExistingTruck() throws Exception {
        int databaseSizeBeforeUpdate = truckRepository.findAll().size();

        // Create the Truck

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTruckMockMvc.perform(put("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Truck in Elasticsearch
        verify(mockTruckSearchRepository, times(0)).save(truck);
    }

    @Test
    @Transactional
    public void deleteTruck() throws Exception {
        // Initialize the database
        truckService.save(truck);

        int databaseSizeBeforeDelete = truckRepository.findAll().size();

        // Get the truck
        restTruckMockMvc.perform(delete("/api/trucks/{id}", truck.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Truck in Elasticsearch
        verify(mockTruckSearchRepository, times(1)).deleteById(truck.getId());
    }

    @Test
    @Transactional
    public void searchTruck() throws Exception {
        // Initialize the database
        truckService.save(truck);
        when(mockTruckSearchRepository.search(queryStringQuery("id:" + truck.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(truck), PageRequest.of(0, 1), 1));
        // Search the truck
        restTruckMockMvc.perform(get("/api/_search/trucks?query=id:" + truck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truck.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].emissionStandard").value(hasItem(DEFAULT_EMISSION_STANDARD.toString())))
            .andExpect(jsonPath("$.[*].horsePower").value(hasItem(DEFAULT_HORSE_POWER)))
            .andExpect(jsonPath("$.[*].fuelTank").value(hasItem(DEFAULT_FUEL_TANK)))
            .andExpect(jsonPath("$.[*].technicalExamDate").value(hasItem(DEFAULT_TECHNICAL_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].compressor").value(hasItem(DEFAULT_COMPRESSOR.booleanValue())))
            .andExpect(jsonPath("$.[*].hydraulics").value(hasItem(DEFAULT_HYDRAULICS.booleanValue())))
            .andExpect(jsonPath("$.[*].gps").value(hasItem(DEFAULT_GPS.booleanValue())))
            .andExpect(jsonPath("$.[*].internationalLicense").value(hasItem(DEFAULT_INTERNATIONAL_LICENSE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Truck.class);
        Truck truck1 = new Truck();
        truck1.setId(1L);
        Truck truck2 = new Truck();
        truck2.setId(truck1.getId());
        assertThat(truck1).isEqualTo(truck2);
        truck2.setId(2L);
        assertThat(truck1).isNotEqualTo(truck2);
        truck1.setId(null);
        assertThat(truck1).isNotEqualTo(truck2);
    }
}
