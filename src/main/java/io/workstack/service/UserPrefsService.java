package io.workstack.service;

import io.workstack.domain.UserPrefs;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserPrefs.
 */
public interface UserPrefsService {

    /**
     * Save a userPrefs.
     *
     * @param userPrefs the entity to save
     * @return the persisted entity
     */
    UserPrefs save(UserPrefs userPrefs);

    /**
     * Get all the userPrefs.
     *
     * @return the list of entities
     */
    List<UserPrefs> findAll();


    /**
     * Get the "id" userPrefs.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserPrefs> findOne(Long id);

    /**
     * Delete the "id" userPrefs.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userPrefs corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<UserPrefs> search(String query);
}
