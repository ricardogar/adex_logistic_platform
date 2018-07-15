package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.Fuel;
import com.foxcode.adex.repository.FuelRepository;
import com.foxcode.adex.repository.search.FuelSearchRepository;
import com.foxcode.adex.service.FuelService;
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
 * Test class for the FuelResource REST controller.
 *
 * @see FuelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class FuelResourceIntTest {

    private static final LocalDate DEFAULT_REFUEL_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REFUEL_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_AMOUNT_FUEL = 1L;
    private static final Long UPDATED_AMOUNT_FUEL = 2L;

    private static final Long DEFAULT_KILOMETERS_STATE = 1L;
    private static final Long UPDATED_KILOMETERS_STATE = 2L;

    private static final Double DEFAULT_FUEL_PRICE = 1D;
    private static final Double UPDATED_FUEL_PRICE = 2D;

    @Autowired
    private FuelRepository fuelRepository;

    

    @Autowired
    private FuelService fuelService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.FuelSearchRepositoryMockConfiguration
     */
    @Autowired
    private FuelSearchRepository mockFuelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFuelMockMvc;

    private Fuel fuel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FuelResource fuelResource = new FuelResource(fuelService);
        this.restFuelMockMvc = MockMvcBuilders.standaloneSetup(fuelResource)
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
    public static Fuel createEntity(EntityManager em) {
        Fuel fuel = new Fuel()
            .refuelData(DEFAULT_REFUEL_DATA)
            .amountFuel(DEFAULT_AMOUNT_FUEL)
            .kilometersState(DEFAULT_KILOMETERS_STATE)
            .fuelPrice(DEFAULT_FUEL_PRICE);
        return fuel;
    }

    @Before
    public void initTest() {
        fuel = createEntity(em);
    }

    @Test
    @Transactional
    public void createFuel() throws Exception {
        int databaseSizeBeforeCreate = fuelRepository.findAll().size();

        // Create the Fuel
        restFuelMockMvc.perform(post("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isCreated());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeCreate + 1);
        Fuel testFuel = fuelList.get(fuelList.size() - 1);
        assertThat(testFuel.getRefuelData()).isEqualTo(DEFAULT_REFUEL_DATA);
        assertThat(testFuel.getAmountFuel()).isEqualTo(DEFAULT_AMOUNT_FUEL);
        assertThat(testFuel.getKilometersState()).isEqualTo(DEFAULT_KILOMETERS_STATE);
        assertThat(testFuel.getFuelPrice()).isEqualTo(DEFAULT_FUEL_PRICE);

        // Validate the Fuel in Elasticsearch
        verify(mockFuelSearchRepository, times(1)).save(testFuel);
    }

    @Test
    @Transactional
    public void createFuelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fuelRepository.findAll().size();

        // Create the Fuel with an existing ID
        fuel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuelMockMvc.perform(post("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeCreate);

        // Validate the Fuel in Elasticsearch
        verify(mockFuelSearchRepository, times(0)).save(fuel);
    }

    @Test
    @Transactional
    public void checkRefuelDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = fuelRepository.findAll().size();
        // set the field null
        fuel.setRefuelData(null);

        // Create the Fuel, which fails.

        restFuelMockMvc.perform(post("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountFuelIsRequired() throws Exception {
        int databaseSizeBeforeTest = fuelRepository.findAll().size();
        // set the field null
        fuel.setAmountFuel(null);

        // Create the Fuel, which fails.

        restFuelMockMvc.perform(post("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKilometersStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fuelRepository.findAll().size();
        // set the field null
        fuel.setKilometersState(null);

        // Create the Fuel, which fails.

        restFuelMockMvc.perform(post("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFuelPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = fuelRepository.findAll().size();
        // set the field null
        fuel.setFuelPrice(null);

        // Create the Fuel, which fails.

        restFuelMockMvc.perform(post("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFuels() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        // Get all the fuelList
        restFuelMockMvc.perform(get("/api/fuels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fuel.getId().intValue())))
            .andExpect(jsonPath("$.[*].refuelData").value(hasItem(DEFAULT_REFUEL_DATA.toString())))
            .andExpect(jsonPath("$.[*].amountFuel").value(hasItem(DEFAULT_AMOUNT_FUEL.intValue())))
            .andExpect(jsonPath("$.[*].kilometersState").value(hasItem(DEFAULT_KILOMETERS_STATE.intValue())))
            .andExpect(jsonPath("$.[*].fuelPrice").value(hasItem(DEFAULT_FUEL_PRICE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getFuel() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        // Get the fuel
        restFuelMockMvc.perform(get("/api/fuels/{id}", fuel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fuel.getId().intValue()))
            .andExpect(jsonPath("$.refuelData").value(DEFAULT_REFUEL_DATA.toString()))
            .andExpect(jsonPath("$.amountFuel").value(DEFAULT_AMOUNT_FUEL.intValue()))
            .andExpect(jsonPath("$.kilometersState").value(DEFAULT_KILOMETERS_STATE.intValue()))
            .andExpect(jsonPath("$.fuelPrice").value(DEFAULT_FUEL_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFuel() throws Exception {
        // Get the fuel
        restFuelMockMvc.perform(get("/api/fuels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFuel() throws Exception {
        // Initialize the database
        fuelService.save(fuel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFuelSearchRepository);

        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();

        // Update the fuel
        Fuel updatedFuel = fuelRepository.findById(fuel.getId()).get();
        // Disconnect from session so that the updates on updatedFuel are not directly saved in db
        em.detach(updatedFuel);
        updatedFuel
            .refuelData(UPDATED_REFUEL_DATA)
            .amountFuel(UPDATED_AMOUNT_FUEL)
            .kilometersState(UPDATED_KILOMETERS_STATE)
            .fuelPrice(UPDATED_FUEL_PRICE);

        restFuelMockMvc.perform(put("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFuel)))
            .andExpect(status().isOk());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
        Fuel testFuel = fuelList.get(fuelList.size() - 1);
        assertThat(testFuel.getRefuelData()).isEqualTo(UPDATED_REFUEL_DATA);
        assertThat(testFuel.getAmountFuel()).isEqualTo(UPDATED_AMOUNT_FUEL);
        assertThat(testFuel.getKilometersState()).isEqualTo(UPDATED_KILOMETERS_STATE);
        assertThat(testFuel.getFuelPrice()).isEqualTo(UPDATED_FUEL_PRICE);

        // Validate the Fuel in Elasticsearch
        verify(mockFuelSearchRepository, times(1)).save(testFuel);
    }

    @Test
    @Transactional
    public void updateNonExistingFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();

        // Create the Fuel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFuelMockMvc.perform(put("/api/fuels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Fuel in Elasticsearch
        verify(mockFuelSearchRepository, times(0)).save(fuel);
    }

    @Test
    @Transactional
    public void deleteFuel() throws Exception {
        // Initialize the database
        fuelService.save(fuel);

        int databaseSizeBeforeDelete = fuelRepository.findAll().size();

        // Get the fuel
        restFuelMockMvc.perform(delete("/api/fuels/{id}", fuel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Fuel in Elasticsearch
        verify(mockFuelSearchRepository, times(1)).deleteById(fuel.getId());
    }

    @Test
    @Transactional
    public void searchFuel() throws Exception {
        // Initialize the database
        fuelService.save(fuel);
        when(mockFuelSearchRepository.search(queryStringQuery("id:" + fuel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fuel), PageRequest.of(0, 1), 1));
        // Search the fuel
        restFuelMockMvc.perform(get("/api/_search/fuels?query=id:" + fuel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fuel.getId().intValue())))
            .andExpect(jsonPath("$.[*].refuelData").value(hasItem(DEFAULT_REFUEL_DATA.toString())))
            .andExpect(jsonPath("$.[*].amountFuel").value(hasItem(DEFAULT_AMOUNT_FUEL.intValue())))
            .andExpect(jsonPath("$.[*].kilometersState").value(hasItem(DEFAULT_KILOMETERS_STATE.intValue())))
            .andExpect(jsonPath("$.[*].fuelPrice").value(hasItem(DEFAULT_FUEL_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fuel.class);
        Fuel fuel1 = new Fuel();
        fuel1.setId(1L);
        Fuel fuel2 = new Fuel();
        fuel2.setId(fuel1.getId());
        assertThat(fuel1).isEqualTo(fuel2);
        fuel2.setId(2L);
        assertThat(fuel1).isNotEqualTo(fuel2);
        fuel1.setId(null);
        assertThat(fuel1).isNotEqualTo(fuel2);
    }
}
