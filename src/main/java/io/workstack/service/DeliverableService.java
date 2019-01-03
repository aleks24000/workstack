package io.workstack.service;

import io.workstack.domain.Deliverable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Deliverable.
 */
public interface DeliverableService {

    /**
     * Save a deliverable.
     *
     * @param deliverable the entity to save
     * @return the persisted entity
     */
    Deliverable save(Deliverable deliverable);

    /**
     * Get all the deliverables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Deliverable> findAll(Pageable pageable);


    /**
     * Get the "id" deliverable.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Deliverable> findOne(Long id);

    /**
     * Delete the "id" deliverable.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the deliverable corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Deliverable> search(String query, Pageable pageable);
}
