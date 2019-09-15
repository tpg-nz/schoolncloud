package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.TeachingClass;
import co.tpg.catalog.repository.TeachingClassRepository;
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
 * REST controller for managing {@link co.tpg.catalog.domain.TeachingClass}.
 */
@RestController
@RequestMapping("/api")
public class TeachingClassResource {

    private final Logger log = LoggerFactory.getLogger(TeachingClassResource.class);

    private static final String ENTITY_NAME = "teachingClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeachingClassRepository teachingClassRepository;

    public TeachingClassResource(TeachingClassRepository teachingClassRepository) {
        this.teachingClassRepository = teachingClassRepository;
    }

    /**
     * {@code POST  /teaching-classes} : Create a new teachingClass.
     *
     * @param teachingClass the teachingClass to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teachingClass, or with status {@code 400 (Bad Request)} if the teachingClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teaching-classes")
    public ResponseEntity<TeachingClass> createTeachingClass(@Valid @RequestBody TeachingClass teachingClass) throws URISyntaxException {
        log.debug("REST request to save TeachingClass : {}", teachingClass);
        if (teachingClass.getId() != null) {
            throw new BadRequestAlertException("A new teachingClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeachingClass result = teachingClassRepository.save(teachingClass);
        return ResponseEntity.created(new URI("/api/teaching-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teaching-classes} : Updates an existing teachingClass.
     *
     * @param teachingClass the teachingClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachingClass,
     * or with status {@code 400 (Bad Request)} if the teachingClass is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teachingClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teaching-classes")
    public ResponseEntity<TeachingClass> updateTeachingClass(@Valid @RequestBody TeachingClass teachingClass) throws URISyntaxException {
        log.debug("REST request to update TeachingClass : {}", teachingClass);
        if (teachingClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeachingClass result = teachingClassRepository.save(teachingClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachingClass.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /teaching-classes} : get all the teachingClasses.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachingClasses in body.
     */
    @GetMapping("/teaching-classes")
    public List<TeachingClass> getAllTeachingClasses() {
        log.debug("REST request to get all TeachingClasses");
        return teachingClassRepository.findAll();
    }

    /**
     * {@code GET  /teaching-classes/:id} : get the "id" teachingClass.
     *
     * @param id the id of the teachingClass to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teachingClass, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teaching-classes/{id}")
    public ResponseEntity<TeachingClass> getTeachingClass(@PathVariable Long id) {
        log.debug("REST request to get TeachingClass : {}", id);
        Optional<TeachingClass> teachingClass = teachingClassRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(teachingClass);
    }

    /**
     * {@code DELETE  /teaching-classes/:id} : delete the "id" teachingClass.
     *
     * @param id the id of the teachingClass to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teaching-classes/{id}")
    public ResponseEntity<Void> deleteTeachingClass(@PathVariable Long id) {
        log.debug("REST request to delete TeachingClass : {}", id);
        teachingClassRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
