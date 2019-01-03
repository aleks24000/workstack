package io.workstack.service.impl;

import io.workstack.service.DeliverableService;
import io.workstack.domain.Deliverable;
import io.workstack.repository.DeliverableRepository;
import io.workstack.repository.search.DeliverableSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Deliverable.
 */
@Service
@Transactional
public class DeliverableServiceImpl implements DeliverableService {

    private final Logger log = LoggerFactory.getLogger(DeliverableServiceImpl.class);

    private final DeliverableRepository deliverableRepository;

    private final DeliverableSearchRepository deliverableSearchRepository;

    public DeliverableServiceImpl(DeliverableRepository deliverableRepository, DeliverableSearchRepository deliverableSearchRepository) {
        this.deliverableRepository = deliverableRepository;
        this.deliverableSearchRepository = deliverableSearchRepository;
    }

    /**
     * Save a deliverable.
     *
     * @param deliverable the entity to save
     * @return the persisted entity
     */
    @Override
    public Deliverable save(Deliverable deliverable) {
        log.debug("Request to save Deliverable : {}", deliverable);
        Deliverable result = deliverableRepository.save(deliverable);
        deliverableSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the deliverables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Deliverable> findAll(Pageable pageable) {
        log.debug("Request to get all Deliverables");
        return deliverableRepository.findAll(pageable);
    }


    /**
     * Get one deliverable by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Deliverable> findOne(Long id) {
        log.debug("Request to get Deliverable : {}", id);
        return deliverableRepository.findById(id);
    }

    /**
     * Delete the deliverable by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deliverable : {}", id);
        deliverableRepository.deleteById(id);
        deliverableSearchRepository.deleteById(id);
    }

    /**
     * Search for the deliverable corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Deliverable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Deliverables for query {}", query);
        return deliverableSearchRepository.search(queryStringQuery(query), pageable);    }
}
