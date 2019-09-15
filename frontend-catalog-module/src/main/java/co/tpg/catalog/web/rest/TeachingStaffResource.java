package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.TeachingStaff;
import co.tpg.catalog.repository.TeachingStaffRepository;
import co.tpg.catalog.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link co.tpg.catalog.domain.TeachingStaff}.
 */
@RestController
@RequestMapping("/api")
public class TeachingStaffResource {

    private final Logger log = LoggerFactory.getLogger(TeachingStaffResource.class);

    private static final String ENTITY_NAME = "teachingStaff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeachingStaffRepository teachingStaffRepository;

    public TeachingStaffResource(TeachingStaffRepository teachingStaffRepository) {
        this.teachingStaffRepository = teachingStaffRepository;
    }

    /**
     * {@code POST  /teaching-staffs} : Create a new teachingStaff.
     *
     * @param teachingStaff the teachingStaff to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teachingStaff, or with status {@code 400 (Bad Request)} if the teachingStaff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teaching-staffs")
    public ResponseEntity<TeachingStaff> createTeachingStaff(@Valid @RequestBody TeachingStaff teachingStaff) throws URISyntaxException {
        log.debug("REST request to save TeachingStaff : {}", teachingStaff);
        if (teachingStaff.getId() != null) {
            throw new BadRequestAlertException("A new teachingStaff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeachingStaff result = teachingStaffRepository.save(teachingStaff);
        return ResponseEntity.created(new URI("/api/teaching-staffs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teaching-staffs} : Updates an existing teachingStaff.
     *
     * @param teachingStaff the teachingStaff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachingStaff,
     * or with status {@code 400 (Bad Request)} if the teachingStaff is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teachingStaff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teaching-staffs")
    public ResponseEntity<TeachingStaff> updateTeachingStaff(@Valid @RequestBody TeachingStaff teachingStaff) throws URISyntaxException {
        log.debug("REST request to update TeachingStaff : {}", teachingStaff);
        if (teachingStaff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeachingStaff result = teachingStaffRepository.save(teachingStaff);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachingStaff.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /teaching-staffs} : get all the teachingStaffs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachingStaffs in body.
     */
    @GetMapping("/teaching-staffs")
    public List<TeachingStaff> getAllTeachingStaffs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all TeachingStaffs");
        return teachingStaffRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /teaching-staffs/:id} : get the "id" teachingStaff.
     *
     * @param id the id of the teachingStaff to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teachingStaff, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teaching-staffs/{id}")
    public ResponseEntity<TeachingStaff> getTeachingStaff(@PathVariable Long id) {
        log.debug("REST request to get TeachingStaff : {}", id);
        Optional<TeachingStaff> teachingStaff = teachingStaffRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(teachingStaff);
    }

    /**
     * {@code DELETE  /teaching-staffs/:id} : delete the "id" teachingStaff.
     *
     * @param id the id of the teachingStaff to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teaching-staffs/{id}")
    public ResponseEntity<Void> deleteTeachingStaff(@PathVariable Long id) {
        log.debug("REST request to delete TeachingStaff : {}", id);
        teachingStaffRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
