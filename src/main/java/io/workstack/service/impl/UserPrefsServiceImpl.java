package io.workstack.service.impl;

import io.workstack.service.UserPrefsService;
import io.workstack.domain.UserPrefs;
import io.workstack.repository.UserPrefsRepository;
import io.workstack.repository.search.UserPrefsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserPrefs.
 */
@Service
@Transactional
public class UserPrefsServiceImpl implements UserPrefsService {

    private final Logger log = LoggerFactory.getLogger(UserPrefsServiceImpl.class);

    private final UserPrefsRepository userPrefsRepository;

    private final UserPrefsSearchRepository userPrefsSearchRepository;

    public UserPrefsServiceImpl(UserPrefsRepository userPrefsRepository, UserPrefsSearchRepository userPrefsSearchRepository) {
        this.userPrefsRepository = userPrefsRepository;
        this.userPrefsSearchRepository = userPrefsSearchRepository;
    }

    /**
     * Save a userPrefs.
     *
     * @param userPrefs the entity to save
     * @return the persisted entity
     */
    @Override
    public UserPrefs save(UserPrefs userPrefs) {
        log.debug("Request to save UserPrefs : {}", userPrefs);
        UserPrefs result = userPrefsRepository.save(userPrefs);
        userPrefsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userPrefs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserPrefs> findAll() {
        log.debug("Request to get all UserPrefs");
        return userPrefsRepository.findAll();
    }


    /**
     * Get one userPrefs by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserPrefs> findOne(Long id) {
        log.debug("Request to get UserPrefs : {}", id);
        return userPrefsRepository.findById(id);
    }

    /**
     * Delete the userPrefs by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserPrefs : {}", id);
        userPrefsRepository.deleteById(id);
        userPrefsSearchRepository.deleteById(id);
    }

    /**
     * Search for the userPrefs corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserPrefs> search(String query) {
        log.debug("Request to search UserPrefs for query {}", query);
        return StreamSupport
            .stream(userPrefsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
