package co.tpg.workflow.web.rest;

import co.tpg.workflow.domain.StepField;
import co.tpg.workflow.service.StepFieldService;
import co.tpg.workflow.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link co.tpg.workflow.domain.StepField}.
 */
@RestController
@RequestMapping("/api")
public class StepFieldResource {

    private final Logger log = LoggerFactory.getLogger(StepFieldResource.class);

    private static final String ENTITY_NAME = "stepField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StepFieldService stepFieldService;

    public StepFieldResource(StepFieldService stepFieldService) {
        this.stepFieldService = stepFieldService;
    }

    /**
     * {@code POST  /step-fields} : Create a new stepField.
     *
     * @param stepField the stepField to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stepField, or with status {@code 400 (Bad Request)} if the stepField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/step-fields")
    public ResponseEntity<StepField> createStepField(@Valid @RequestBody StepField stepField) throws URISyntaxException {
        log.debug("REST request to save StepField : {}", stepField);
        if (stepField.getId() != null) {
            throw new BadRequestAlertException("A new stepField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StepField result = stepFieldService.save(stepField);
        return ResponseEntity.created(new URI("/api/step-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /step-fields} : Updates an existing stepField.
     *
     * @param stepField the stepField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stepField,
     * or with status {@code 400 (Bad Request)} if the stepField is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stepField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/step-fields")
    public ResponseEntity<StepField> updateStepField(@Valid @RequestBody StepField stepField) throws URISyntaxException {
        log.debug("REST request to update StepField : {}", stepField);
        if (stepField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StepField result = stepFieldService.save(stepField);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stepField.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /step-fields} : get all the stepFields.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stepFields in body.
     */
    @GetMapping("/step-fields")
    public ResponseEntity<List<StepField>> getAllStepFields(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of StepFields");
        Page<StepField> page = stepFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /step-fields/:id} : get the "id" stepField.
     *
     * @param id the id of the stepField to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stepField, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/step-fields/{id}")
    public ResponseEntity<StepField> getStepField(@PathVariable Long id) {
        log.debug("REST request to get StepField : {}", id);
        Optional<StepField> stepField = stepFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stepField);
    }

    /**
     * {@code DELETE  /step-fields/:id} : delete the "id" stepField.
     *
     * @param id the id of the stepField to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/step-fields/{id}")
    public ResponseEntity<Void> deleteStepField(@PathVariable Long id) {
        log.debug("REST request to delete StepField : {}", id);
        stepFieldService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
