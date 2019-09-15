package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.EducationalInstituition;
import co.tpg.catalog.repository.EducationalInstituitionRepository;
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
 * REST controller for managing {@link co.tpg.catalog.domain.EducationalInstituition}.
 */
@RestController
@RequestMapping("/api")
public class EducationalInstituitionResource {

    private final Logger log = LoggerFactory.getLogger(EducationalInstituitionResource.class);

    private static final String ENTITY_NAME = "educationalInstituition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationalInstituitionRepository educationalInstituitionRepository;

    public EducationalInstituitionResource(EducationalInstituitionRepository educationalInstituitionRepository) {
        this.educationalInstituitionRepository = educationalInstituitionRepository;
    }

    /**
     * {@code POST  /educational-instituitions} : Create a new educationalInstituition.
     *
     * @param educationalInstituition the educationalInstituition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationalInstituition, or with status {@code 400 (Bad Request)} if the educationalInstituition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/educational-instituitions")
    public ResponseEntity<EducationalInstituition> createEducationalInstituition(@Valid @RequestBody EducationalInstituition educationalInstituition) throws URISyntaxException {
        log.debug("REST request to save EducationalInstituition : {}", educationalInstituition);
        if (educationalInstituition.getId() != null) {
            throw new BadRequestAlertException("A new educationalInstituition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EducationalInstituition result = educationalInstituitionRepository.save(educationalInstituition);
        return ResponseEntity.created(new URI("/api/educational-instituitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /educational-instituitions} : Updates an existing educationalInstituition.
     *
     * @param educationalInstituition the educationalInstituition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationalInstituition,
     * or with status {@code 400 (Bad Request)} if the educationalInstituition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationalInstituition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/educational-instituitions")
    public ResponseEntity<EducationalInstituition> updateEducationalInstituition(@Valid @RequestBody EducationalInstituition educationalInstituition) throws URISyntaxException {
        log.debug("REST request to update EducationalInstituition : {}", educationalInstituition);
        if (educationalInstituition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EducationalInstituition result = educationalInstituitionRepository.save(educationalInstituition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationalInstituition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /educational-instituitions} : get all the educationalInstituitions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationalInstituitions in body.
     */
    @GetMapping("/educational-instituitions")
    public List<EducationalInstituition> getAllEducationalInstituitions() {
        log.debug("REST request to get all EducationalInstituitions");
        return educationalInstituitionRepository.findAll();
    }

    /**
     * {@code GET  /educational-instituitions/:id} : get the "id" educationalInstituition.
     *
     * @param id the id of the educationalInstituition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationalInstituition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/educational-instituitions/{id}")
    public ResponseEntity<EducationalInstituition> getEducationalInstituition(@PathVariable Long id) {
        log.debug("REST request to get EducationalInstituition : {}", id);
        Optional<EducationalInstituition> educationalInstituition = educationalInstituitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(educationalInstituition);
    }

    /**
     * {@code DELETE  /educational-instituitions/:id} : delete the "id" educationalInstituition.
     *
     * @param id the id of the educationalInstituition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/educational-instituitions/{id}")
    public ResponseEntity<Void> deleteEducationalInstituition(@PathVariable Long id) {
        log.debug("REST request to delete EducationalInstituition : {}", id);
        educationalInstituitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
