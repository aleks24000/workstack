package io.workstack.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.workstack.domain.UserPrefs;
import io.workstack.service.UserPrefsService;
import io.workstack.web.rest.errors.BadRequestAlertException;
import io.workstack.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UserPrefs.
 */
@RestController
@RequestMapping("/api")
public class UserPrefsResource {

    private final Logger log = LoggerFactory.getLogger(UserPrefsResource.class);

    private static final String ENTITY_NAME = "userPrefs";

    private final UserPrefsService userPrefsService;

    public UserPrefsResource(UserPrefsService userPrefsService) {
        this.userPrefsService = userPrefsService;
    }

    /**
     * POST  /user-prefs : Create a new userPrefs.
     *
     * @param userPrefs the userPrefs to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userPrefs, or with status 400 (Bad Request) if the userPrefs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-prefs")
    @Timed
    public ResponseEntity<UserPrefs> createUserPrefs(@RequestBody UserPrefs userPrefs) throws URISyntaxException {
        log.debug("REST request to save UserPrefs : {}", userPrefs);
        if (userPrefs.getId() != null) {
            throw new BadRequestAlertException("A new userPrefs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPrefs result = userPrefsService.save(userPrefs);
        return ResponseEntity.created(new URI("/api/user-prefs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-prefs : Updates an existing userPrefs.
     *
     * @param userPrefs the userPrefs to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userPrefs,
     * or with status 400 (Bad Request) if the userPrefs is not valid,
     * or with status 500 (Internal Server Error) if the userPrefs couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-prefs")
    @Timed
    public ResponseEntity<UserPrefs> updateUserPrefs(@RequestBody UserPrefs userPrefs) throws URISyntaxException {
        log.debug("REST request to update UserPrefs : {}", userPrefs);
        if (userPrefs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserPrefs result = userPrefsService.save(userPrefs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userPrefs.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-prefs : get all the userPrefs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userPrefs in body
     */
    @GetMapping("/user-prefs")
    @Timed
    public List<UserPrefs> getAllUserPrefs() {
        log.debug("REST request to get all UserPrefs");
        return userPrefsService.findAll();
    }

    /**
     * GET  /user-prefs/:id : get the "id" userPrefs.
     *
     * @param id the id of the userPrefs to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userPrefs, or with status 404 (Not Found)
     */
    @GetMapping("/user-prefs/{id}")
    @Timed
    public ResponseEntity<UserPrefs> getUserPrefs(@PathVariable Long id) {
        log.debug("REST request to get UserPrefs : {}", id);
        Optional<UserPrefs> userPrefs = userPrefsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPrefs);
    }

    /**
     * DELETE  /user-prefs/:id : delete the "id" userPrefs.
     *
     * @param id the id of the userPrefs to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-prefs/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserPrefs(@PathVariable Long id) {
        log.debug("REST request to delete UserPrefs : {}", id);
        userPrefsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-prefs?query=:query : search for the userPrefs corresponding
     * to the query.
     *
     * @param query the query of the userPrefs search
     * @return the result of the search
     */
    @GetMapping("/_search/user-prefs")
    @Timed
    public List<UserPrefs> searchUserPrefs(@RequestParam String query) {
        log.debug("REST request to search UserPrefs for query {}", query);
        return userPrefsService.search(query);
    }

}
