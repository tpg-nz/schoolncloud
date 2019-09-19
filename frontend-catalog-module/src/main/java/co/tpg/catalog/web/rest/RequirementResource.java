package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.Requirement;
import co.tpg.catalog.service.RequirementService;
import co.tpg.catalog.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link co.tpg.catalog.domain.Requirement}.
 */
@RestController
@RequestMapping("/api")
public class RequirementResource {

    private final Logger log = LoggerFactory.getLogger(RequirementResource.class);

    private static final String ENTITY_NAME = "requirement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequirementService requirementService;

    public RequirementResource(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    /**
     * {@code POST  /requirements} : Create a new requirement.
     *
     * @param requirement the requirement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requirement, or with status {@code 400 (Bad Request)} if the requirement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requirements")
    public ResponseEntity<Requirement> createRequirement(@Valid @RequestBody Requirement requirement) throws URISyntaxException {
        log.debug("REST request to save Requirement : {}", requirement);
        if (requirement.getId() != null) {
            throw new BadRequestAlertException("A new requirement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Requirement result = requirementService.save(requirement);
        return ResponseEntity.created(new URI("/api/requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requirements} : Updates an existing requirement.
     *
     * @param requirement the requirement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requirement,
     * or with status {@code 400 (Bad Request)} if the requirement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requirement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requirements")
    public ResponseEntity<Requirement> updateRequirement(@Valid @RequestBody Requirement requirement) throws URISyntaxException {
        log.debug("REST request to update Requirement : {}", requirement);
        if (requirement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Requirement result = requirementService.save(requirement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requirement.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requirements} : get all the requirements.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requirements in body.
     */
    @GetMapping("/requirements")
    public ResponseEntity<List<Requirement>> getAllRequirements(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Requirements");
        Page<Requirement> page = requirementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requirements/:id} : get the "id" requirement.
     *
     * @param id the id of the requirement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requirement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requirements/{id}")
    public ResponseEntity<Requirement> getRequirement(@PathVariable Long id) {
        log.debug("REST request to get Requirement : {}", id);
        Optional<Requirement> requirement = requirementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requirement);
    }

    /**
     * {@code DELETE  /requirements/:id} : delete the "id" requirement.
     *
     * @param id the id of the requirement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requirements/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        log.debug("REST request to delete Requirement : {}", id);
        requirementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
