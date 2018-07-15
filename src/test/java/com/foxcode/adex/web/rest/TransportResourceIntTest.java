package com.foxcode.adex.web.rest;

import com.foxcode.adex.AdexPlatformApp;

import com.foxcode.adex.domain.Transport;
import com.foxcode.adex.repository.TransportRepository;
import com.foxcode.adex.repository.search.TransportSearchRepository;
import com.foxcode.adex.service.TransportService;
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

import com.foxcode.adex.domain.enumeration.TransportStatus;
/**
 * Test class for the TransportResource REST controller.
 *
 * @see TransportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdexPlatformApp.class)
public class TransportResourceIntTest {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final TransportStatus DEFAULT_STATUS = TransportStatus.LOAD;
    private static final TransportStatus UPDATED_STATUS = TransportStatus.UNOLADING;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOUSE_NUMBER = 1;
    private static final Integer UPDATED_HOUSE_NUMBER = 2;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PLANNED_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PLANNED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_CHANGING_PLACE_UNLOADING = false;
    private static final Boolean UPDATED_CHANGING_PLACE_UNLOADING = true;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private TransportRepository transportRepository;

    

    @Autowired
    private TransportService transportService;

    /**
     * This repository is mocked in the com.foxcode.adex.repository.search test package.
     *
     * @see com.foxcode.adex.repository.search.TransportSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransportSearchRepository mockTransportSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransportMockMvc;

    private Transport transport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransportResource transportResource = new TransportResource(transportService);
        this.restTransportMockMvc = MockMvcBuilders.standaloneSetup(transportResource)
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
    public static Transport createEntity(EntityManager em) {
        Transport transport = new Transport()
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .createDate(DEFAULT_CREATE_DATE)
            .status(DEFAULT_STATUS)
            .city(DEFAULT_CITY)
            .postCode(DEFAULT_POST_CODE)
            .street(DEFAULT_STREET)
            .houseNumber(DEFAULT_HOUSE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .plannedDeliveryDate(DEFAULT_PLANNED_DELIVERY_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .changingPlaceUnloading(DEFAULT_CHANGING_PLACE_UNLOADING)
            .comments(DEFAULT_COMMENTS);
        return transport;
    }

    @Before
    public void initTest() {
        transport = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransport() throws Exception {
        int databaseSizeBeforeCreate = transportRepository.findAll().size();

        // Create the Transport
        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isCreated());

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeCreate + 1);
        Transport testTransport = transportList.get(transportList.size() - 1);
        assertThat(testTransport.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testTransport.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTransport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransport.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testTransport.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testTransport.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testTransport.getHouseNumber()).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testTransport.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTransport.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTransport.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTransport.getPlannedDeliveryDate()).isEqualTo(DEFAULT_PLANNED_DELIVERY_DATE);
        assertThat(testTransport.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testTransport.isChangingPlaceUnloading()).isEqualTo(DEFAULT_CHANGING_PLACE_UNLOADING);
        assertThat(testTransport.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the Transport in Elasticsearch
        verify(mockTransportSearchRepository, times(1)).save(testTransport);
    }

    @Test
    @Transactional
    public void createTransportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transportRepository.findAll().size();

        // Create the Transport with an existing ID
        transport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeCreate);

        // Validate the Transport in Elasticsearch
        verify(mockTransportSearchRepository, times(0)).save(transport);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setOrderNumber(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setCreateDate(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setStatus(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setCity(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setPostCode(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setStreet(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setLastName(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setPhoneNumber(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeliveryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setDeliveryDate(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChangingPlaceUnloadingIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().size();
        // set the field null
        transport.setChangingPlaceUnloading(null);

        // Create the Transport, which fails.

        restTransportMockMvc.perform(post("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransports() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        // Get all the transportList
        restTransportMockMvc.perform(get("/api/transports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transport.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].plannedDeliveryDate").value(hasItem(DEFAULT_PLANNED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].changingPlaceUnloading").value(hasItem(DEFAULT_CHANGING_PLACE_UNLOADING.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
    

    @Test
    @Transactional
    public void getTransport() throws Exception {
        // Initialize the database
        transportRepository.saveAndFlush(transport);

        // Get the transport
        restTransportMockMvc.perform(get("/api/transports/{id}", transport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transport.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.houseNumber").value(DEFAULT_HOUSE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.plannedDeliveryDate").value(DEFAULT_PLANNED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.changingPlaceUnloading").value(DEFAULT_CHANGING_PLACE_UNLOADING.booleanValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingTransport() throws Exception {
        // Get the transport
        restTransportMockMvc.perform(get("/api/transports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransport() throws Exception {
        // Initialize the database
        transportService.save(transport);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTransportSearchRepository);

        int databaseSizeBeforeUpdate = transportRepository.findAll().size();

        // Update the transport
        Transport updatedTransport = transportRepository.findById(transport.getId()).get();
        // Disconnect from session so that the updates on updatedTransport are not directly saved in db
        em.detach(updatedTransport);
        updatedTransport
            .orderNumber(UPDATED_ORDER_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .status(UPDATED_STATUS)
            .city(UPDATED_CITY)
            .postCode(UPDATED_POST_CODE)
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .plannedDeliveryDate(UPDATED_PLANNED_DELIVERY_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .changingPlaceUnloading(UPDATED_CHANGING_PLACE_UNLOADING)
            .comments(UPDATED_COMMENTS);

        restTransportMockMvc.perform(put("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransport)))
            .andExpect(status().isOk());

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
        Transport testTransport = transportList.get(transportList.size() - 1);
        assertThat(testTransport.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testTransport.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTransport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransport.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTransport.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testTransport.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testTransport.getHouseNumber()).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testTransport.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTransport.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTransport.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTransport.getPlannedDeliveryDate()).isEqualTo(UPDATED_PLANNED_DELIVERY_DATE);
        assertThat(testTransport.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testTransport.isChangingPlaceUnloading()).isEqualTo(UPDATED_CHANGING_PLACE_UNLOADING);
        assertThat(testTransport.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the Transport in Elasticsearch
        verify(mockTransportSearchRepository, times(1)).save(testTransport);
    }

    @Test
    @Transactional
    public void updateNonExistingTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().size();

        // Create the Transport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransportMockMvc.perform(put("/api/transports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transport)))
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Transport in Elasticsearch
        verify(mockTransportSearchRepository, times(0)).save(transport);
    }

    @Test
    @Transactional
    public void deleteTransport() throws Exception {
        // Initialize the database
        transportService.save(transport);

        int databaseSizeBeforeDelete = transportRepository.findAll().size();

        // Get the transport
        restTransportMockMvc.perform(delete("/api/transports/{id}", transport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transport> transportList = transportRepository.findAll();
        assertThat(transportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Transport in Elasticsearch
        verify(mockTransportSearchRepository, times(1)).deleteById(transport.getId());
    }

    @Test
    @Transactional
    public void searchTransport() throws Exception {
        // Initialize the database
        transportService.save(transport);
        when(mockTransportSearchRepository.search(queryStringQuery("id:" + transport.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transport), PageRequest.of(0, 1), 1));
        // Search the transport
        restTransportMockMvc.perform(get("/api/_search/transports?query=id:" + transport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transport.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].plannedDeliveryDate").value(hasItem(DEFAULT_PLANNED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].changingPlaceUnloading").value(hasItem(DEFAULT_CHANGING_PLACE_UNLOADING.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transport.class);
        Transport transport1 = new Transport();
        transport1.setId(1L);
        Transport transport2 = new Transport();
        transport2.setId(transport1.getId());
        assertThat(transport1).isEqualTo(transport2);
        transport2.setId(2L);
        assertThat(transport1).isNotEqualTo(transport2);
        transport1.setId(null);
        assertThat(transport1).isNotEqualTo(transport2);
    }
}
