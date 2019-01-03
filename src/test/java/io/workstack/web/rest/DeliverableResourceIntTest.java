package io.workstack.web.rest;

import io.workstack.WorkstackApp;

import io.workstack.domain.Deliverable;
import io.workstack.repository.DeliverableRepository;
import io.workstack.repository.search.DeliverableSearchRepository;
import io.workstack.service.DeliverableService;
import io.workstack.web.rest.errors.ExceptionTranslator;

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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static io.workstack.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DeliverableResource REST controller.
 *
 * @see DeliverableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstackApp.class)
public class DeliverableResourceIntTest {

    private static final String DEFAULT_TECH_ID = "AAAAAAAAAA";
    private static final String UPDATED_TECH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SPENT_TIME = 1L;
    private static final Long UPDATED_SPENT_TIME = 2L;

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private DeliverableService deliverableService;

    /**
     * This repository is mocked in the io.workstack.repository.search test package.
     *
     * @see io.workstack.repository.search.DeliverableSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeliverableSearchRepository mockDeliverableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDeliverableMockMvc;

    private Deliverable deliverable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeliverableResource deliverableResource = new DeliverableResource(deliverableService);
        this.restDeliverableMockMvc = MockMvcBuilders.standaloneSetup(deliverableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliverable createEntity(EntityManager em) {
        Deliverable deliverable = new Deliverable()
            .techId(DEFAULT_TECH_ID)
            .name(DEFAULT_NAME)
            .spentTime(DEFAULT_SPENT_TIME);
        return deliverable;
    }

    @Before
    public void initTest() {
        deliverable = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeliverable() throws Exception {
        int databaseSizeBeforeCreate = deliverableRepository.findAll().size();

        // Create the Deliverable
        restDeliverableMockMvc.perform(post("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isCreated());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeCreate + 1);
        Deliverable testDeliverable = deliverableList.get(deliverableList.size() - 1);
        assertThat(testDeliverable.getTechId()).isEqualTo(DEFAULT_TECH_ID);
        assertThat(testDeliverable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliverable.getSpentTime()).isEqualTo(DEFAULT_SPENT_TIME);

        // Validate the Deliverable in Elasticsearch
        verify(mockDeliverableSearchRepository, times(1)).save(testDeliverable);
    }

    @Test
    @Transactional
    public void createDeliverableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliverableRepository.findAll().size();

        // Create the Deliverable with an existing ID
        deliverable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliverableMockMvc.perform(post("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isBadRequest());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeCreate);

        // Validate the Deliverable in Elasticsearch
        verify(mockDeliverableSearchRepository, times(0)).save(deliverable);
    }

    @Test
    @Transactional
    public void getAllDeliverables() throws Exception {
        // Initialize the database
        deliverableRepository.saveAndFlush(deliverable);

        // Get all the deliverableList
        restDeliverableMockMvc.perform(get("/api/deliverables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliverable.getId().intValue())))
            .andExpect(jsonPath("$.[*].techId").value(hasItem(DEFAULT_TECH_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].spentTime").value(hasItem(DEFAULT_SPENT_TIME.intValue())));
    }
    
    @Test
    @Transactional
    public void getDeliverable() throws Exception {
        // Initialize the database
        deliverableRepository.saveAndFlush(deliverable);

        // Get the deliverable
        restDeliverableMockMvc.perform(get("/api/deliverables/{id}", deliverable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deliverable.getId().intValue()))
            .andExpect(jsonPath("$.techId").value(DEFAULT_TECH_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.spentTime").value(DEFAULT_SPENT_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeliverable() throws Exception {
        // Get the deliverable
        restDeliverableMockMvc.perform(get("/api/deliverables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliverable() throws Exception {
        // Initialize the database
        deliverableService.save(deliverable);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDeliverableSearchRepository);

        int databaseSizeBeforeUpdate = deliverableRepository.findAll().size();

        // Update the deliverable
        Deliverable updatedDeliverable = deliverableRepository.findById(deliverable.getId()).get();
        // Disconnect from session so that the updates on updatedDeliverable are not directly saved in db
        em.detach(updatedDeliverable);
        updatedDeliverable
            .techId(UPDATED_TECH_ID)
            .name(UPDATED_NAME)
            .spentTime(UPDATED_SPENT_TIME);

        restDeliverableMockMvc.perform(put("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeliverable)))
            .andExpect(status().isOk());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeUpdate);
        Deliverable testDeliverable = deliverableList.get(deliverableList.size() - 1);
        assertThat(testDeliverable.getTechId()).isEqualTo(UPDATED_TECH_ID);
        assertThat(testDeliverable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliverable.getSpentTime()).isEqualTo(UPDATED_SPENT_TIME);

        // Validate the Deliverable in Elasticsearch
        verify(mockDeliverableSearchRepository, times(1)).save(testDeliverable);
    }

    @Test
    @Transactional
    public void updateNonExistingDeliverable() throws Exception {
        int databaseSizeBeforeUpdate = deliverableRepository.findAll().size();

        // Create the Deliverable

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliverableMockMvc.perform(put("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isBadRequest());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Deliverable in Elasticsearch
        verify(mockDeliverableSearchRepository, times(0)).save(deliverable);
    }

    @Test
    @Transactional
    public void deleteDeliverable() throws Exception {
        // Initialize the database
        deliverableService.save(deliverable);

        int databaseSizeBeforeDelete = deliverableRepository.findAll().size();

        // Get the deliverable
        restDeliverableMockMvc.perform(delete("/api/deliverables/{id}", deliverable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Deliverable in Elasticsearch
        verify(mockDeliverableSearchRepository, times(1)).deleteById(deliverable.getId());
    }

    @Test
    @Transactional
    public void searchDeliverable() throws Exception {
        // Initialize the database
        deliverableService.save(deliverable);
        when(mockDeliverableSearchRepository.search(queryStringQuery("id:" + deliverable.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(deliverable), PageRequest.of(0, 1), 1));
        // Search the deliverable
        restDeliverableMockMvc.perform(get("/api/_search/deliverables?query=id:" + deliverable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliverable.getId().intValue())))
            .andExpect(jsonPath("$.[*].techId").value(hasItem(DEFAULT_TECH_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].spentTime").value(hasItem(DEFAULT_SPENT_TIME.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deliverable.class);
        Deliverable deliverable1 = new Deliverable();
        deliverable1.setId(1L);
        Deliverable deliverable2 = new Deliverable();
        deliverable2.setId(deliverable1.getId());
        assertThat(deliverable1).isEqualTo(deliverable2);
        deliverable2.setId(2L);
        assertThat(deliverable1).isNotEqualTo(deliverable2);
        deliverable1.setId(null);
        assertThat(deliverable1).isNotEqualTo(deliverable2);
    }
}
