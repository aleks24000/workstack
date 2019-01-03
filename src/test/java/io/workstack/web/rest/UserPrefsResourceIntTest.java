package io.workstack.web.rest;

import io.workstack.WorkstackApp;

import io.workstack.domain.UserPrefs;
import io.workstack.repository.UserPrefsRepository;
import io.workstack.repository.search.UserPrefsSearchRepository;
import io.workstack.service.UserPrefsService;
import io.workstack.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Test class for the UserPrefsResource REST controller.
 *
 * @see UserPrefsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstackApp.class)
public class UserPrefsResourceIntTest {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    @Autowired
    private UserPrefsRepository userPrefsRepository;

    @Autowired
    private UserPrefsService userPrefsService;

    /**
     * This repository is mocked in the io.workstack.repository.search test package.
     *
     * @see io.workstack.repository.search.UserPrefsSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserPrefsSearchRepository mockUserPrefsSearchRepository;

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

    private MockMvc restUserPrefsMockMvc;

    private UserPrefs userPrefs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserPrefsResource userPrefsResource = new UserPrefsResource(userPrefsService);
        this.restUserPrefsMockMvc = MockMvcBuilders.standaloneSetup(userPrefsResource)
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
    public static UserPrefs createEntity(EntityManager em) {
        UserPrefs userPrefs = new UserPrefs()
            .login(DEFAULT_LOGIN);
        return userPrefs;
    }

    @Before
    public void initTest() {
        userPrefs = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPrefs() throws Exception {
        int databaseSizeBeforeCreate = userPrefsRepository.findAll().size();

        // Create the UserPrefs
        restUserPrefsMockMvc.perform(post("/api/user-prefs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPrefs)))
            .andExpect(status().isCreated());

        // Validate the UserPrefs in the database
        List<UserPrefs> userPrefsList = userPrefsRepository.findAll();
        assertThat(userPrefsList).hasSize(databaseSizeBeforeCreate + 1);
        UserPrefs testUserPrefs = userPrefsList.get(userPrefsList.size() - 1);
        assertThat(testUserPrefs.getLogin()).isEqualTo(DEFAULT_LOGIN);

        // Validate the UserPrefs in Elasticsearch
        verify(mockUserPrefsSearchRepository, times(1)).save(testUserPrefs);
    }

    @Test
    @Transactional
    public void createUserPrefsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPrefsRepository.findAll().size();

        // Create the UserPrefs with an existing ID
        userPrefs.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPrefsMockMvc.perform(post("/api/user-prefs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPrefs)))
            .andExpect(status().isBadRequest());

        // Validate the UserPrefs in the database
        List<UserPrefs> userPrefsList = userPrefsRepository.findAll();
        assertThat(userPrefsList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserPrefs in Elasticsearch
        verify(mockUserPrefsSearchRepository, times(0)).save(userPrefs);
    }

    @Test
    @Transactional
    public void getAllUserPrefs() throws Exception {
        // Initialize the database
        userPrefsRepository.saveAndFlush(userPrefs);

        // Get all the userPrefsList
        restUserPrefsMockMvc.perform(get("/api/user-prefs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPrefs.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())));
    }
    
    @Test
    @Transactional
    public void getUserPrefs() throws Exception {
        // Initialize the database
        userPrefsRepository.saveAndFlush(userPrefs);

        // Get the userPrefs
        restUserPrefsMockMvc.perform(get("/api/user-prefs/{id}", userPrefs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userPrefs.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPrefs() throws Exception {
        // Get the userPrefs
        restUserPrefsMockMvc.perform(get("/api/user-prefs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPrefs() throws Exception {
        // Initialize the database
        userPrefsService.save(userPrefs);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUserPrefsSearchRepository);

        int databaseSizeBeforeUpdate = userPrefsRepository.findAll().size();

        // Update the userPrefs
        UserPrefs updatedUserPrefs = userPrefsRepository.findById(userPrefs.getId()).get();
        // Disconnect from session so that the updates on updatedUserPrefs are not directly saved in db
        em.detach(updatedUserPrefs);
        updatedUserPrefs
            .login(UPDATED_LOGIN);

        restUserPrefsMockMvc.perform(put("/api/user-prefs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserPrefs)))
            .andExpect(status().isOk());

        // Validate the UserPrefs in the database
        List<UserPrefs> userPrefsList = userPrefsRepository.findAll();
        assertThat(userPrefsList).hasSize(databaseSizeBeforeUpdate);
        UserPrefs testUserPrefs = userPrefsList.get(userPrefsList.size() - 1);
        assertThat(testUserPrefs.getLogin()).isEqualTo(UPDATED_LOGIN);

        // Validate the UserPrefs in Elasticsearch
        verify(mockUserPrefsSearchRepository, times(1)).save(testUserPrefs);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPrefs() throws Exception {
        int databaseSizeBeforeUpdate = userPrefsRepository.findAll().size();

        // Create the UserPrefs

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPrefsMockMvc.perform(put("/api/user-prefs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPrefs)))
            .andExpect(status().isBadRequest());

        // Validate the UserPrefs in the database
        List<UserPrefs> userPrefsList = userPrefsRepository.findAll();
        assertThat(userPrefsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserPrefs in Elasticsearch
        verify(mockUserPrefsSearchRepository, times(0)).save(userPrefs);
    }

    @Test
    @Transactional
    public void deleteUserPrefs() throws Exception {
        // Initialize the database
        userPrefsService.save(userPrefs);

        int databaseSizeBeforeDelete = userPrefsRepository.findAll().size();

        // Get the userPrefs
        restUserPrefsMockMvc.perform(delete("/api/user-prefs/{id}", userPrefs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPrefs> userPrefsList = userPrefsRepository.findAll();
        assertThat(userPrefsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserPrefs in Elasticsearch
        verify(mockUserPrefsSearchRepository, times(1)).deleteById(userPrefs.getId());
    }

    @Test
    @Transactional
    public void searchUserPrefs() throws Exception {
        // Initialize the database
        userPrefsService.save(userPrefs);
        when(mockUserPrefsSearchRepository.search(queryStringQuery("id:" + userPrefs.getId())))
            .thenReturn(Collections.singletonList(userPrefs));
        // Search the userPrefs
        restUserPrefsMockMvc.perform(get("/api/_search/user-prefs?query=id:" + userPrefs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPrefs.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPrefs.class);
        UserPrefs userPrefs1 = new UserPrefs();
        userPrefs1.setId(1L);
        UserPrefs userPrefs2 = new UserPrefs();
        userPrefs2.setId(userPrefs1.getId());
        assertThat(userPrefs1).isEqualTo(userPrefs2);
        userPrefs2.setId(2L);
        assertThat(userPrefs1).isNotEqualTo(userPrefs2);
        userPrefs1.setId(null);
        assertThat(userPrefs1).isNotEqualTo(userPrefs2);
    }
}
