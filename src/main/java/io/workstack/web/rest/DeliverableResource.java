package io.workstack.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.workstack.domain.Deliverable;
import io.workstack.service.DeliverableService;
import io.workstack.web.rest.errors.BadRequestAlertException;
import io.workstack.web.rest.util.HeaderUtil;
import io.workstack.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Deliverable.
 */
@RestController
@RequestMapping("/api")
public class DeliverableResource {

    private final Logger log = LoggerFactory.getLogger(DeliverableResource.class);

    private static final String ENTITY_NAME = "deliverable";

    private final DeliverableService deliverableService;

    public DeliverableResource(DeliverableService deliverableService) {
        this.deliverableService = deliverableService;
    }

    /**
     * POST  /deliverables : Create a new deliverable.
     *
     * @param deliverable the deliverable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deliverable, or with status 400 (Bad Request) if the deliverable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/deliverables")
    @Timed
    public ResponseEntity<Deliverable> createDeliverable(@RequestBody Deliverable deliverable) throws URISyntaxException {
        log.debug("REST request to save Deliverable : {}", deliverable);
        if (deliverable.getId() != null) {
            throw new BadRequestAlertException("A new deliverable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deliverable result = deliverableService.save(deliverable);
        return ResponseEntity.created(new URI("/api/deliverables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deliverables : Updates an existing deliverable.
     *
     * @param deliverable the deliverable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deliverable,
     * or with status 400 (Bad Request) if the deliverable is not valid,
     * or with status 500 (Internal Server Error) if the deliverable couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/deliverables")
    @Timed
    public ResponseEntity<Deliverable> updateDeliverable(@RequestBody Deliverable deliverable) throws URISyntaxException {
        log.debug("REST request to update Deliverable : {}", deliverable);
        if (deliverable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Deliverable result = deliverableService.save(deliverable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deliverable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deliverables : get all the deliverables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deliverables in body
     */
    @GetMapping("/deliverables")
    @Timed
    public ResponseEntity<List<Deliverable>> getAllDeliverables(Pageable pageable) {
        log.debug("REST request to get a page of Deliverables");
        Page<Deliverable> page = deliverableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/deliverables");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /deliverables/:id : get the "id" deliverable.
     *
     * @param id the id of the deliverable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deliverable, or with status 404 (Not Found)
     */
    @GetMapping("/deliverables/{id}")
    @Timed
    public ResponseEntity<Deliverable> getDeliverable(@PathVariable Long id) {
        log.debug("REST request to get Deliverable : {}", id);
        Optional<Deliverable> deliverable = deliverableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliverable);
    }

    /**
     * DELETE  /deliverables/:id : delete the "id" deliverable.
     *
     * @param id the id of the deliverable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/deliverables/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeliverable(@PathVariable Long id) {
        log.debug("REST request to delete Deliverable : {}", id);
        deliverableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/deliverables?query=:query : search for the deliverable corresponding
     * to the query.
     *
     * @param query the query of the deliverable search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/deliverables")
    @Timed
    public ResponseEntity<List<Deliverable>> searchDeliverables(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Deliverables for query {}", query);
        Page<Deliverable> page = deliverableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/deliverables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
