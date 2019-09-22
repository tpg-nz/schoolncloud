package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.EducationalInstitution;
import co.tpg.catalog.service.EducationalInstitutionService;
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
 * REST controller for managing {@link co.tpg.catalog.domain.EducationalInstitution}.
 */
@RestController
@RequestMapping("/api")
public class EducationalInstitutionResource {

    private final Logger log = LoggerFactory.getLogger(EducationalInstitutionResource.class);

    private static final String ENTITY_NAME = "educationalInstitution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationalInstitutionService educationalInstitutionService;

    public EducationalInstitutionResource(EducationalInstitutionService educationalInstitutionService) {
        this.educationalInstitutionService = educationalInstitutionService;
    }

    /**
     * {@code POST  /educational-institutions} : Create a new educationalInstitution.
     *
     * @param educationalInstitution the educationalInstitution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationalInstitution, or with status {@code 400 (Bad Request)} if the educationalInstitution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/educational-institutions")
    public ResponseEntity<EducationalInstitution> createEducationalInstitution(@Valid @RequestBody EducationalInstitution educationalInstitution) throws URISyntaxException {
        log.debug("REST request to save EducationalInstitution : {}", educationalInstitution);
        if (educationalInstitution.getId() != null) {
            throw new BadRequestAlertException("A new educationalInstitution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EducationalInstitution result = educationalInstitutionService.save(educationalInstitution);
        return ResponseEntity.created(new URI("/api/educational-institutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /educational-institutions} : Updates an existing educationalInstitution.
     *
     * @param educationalInstitution the educationalInstitution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationalInstitution,
     * or with status {@code 400 (Bad Request)} if the educationalInstitution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationalInstitution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/educational-institutions")
    public ResponseEntity<EducationalInstitution> updateEducationalInstitution(@Valid @RequestBody EducationalInstitution educationalInstitution) throws URISyntaxException {
        log.debug("REST request to update EducationalInstitution : {}", educationalInstitution);
        if (educationalInstitution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EducationalInstitution result = educationalInstitutionService.save(educationalInstitution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationalInstitution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /educational-institutions} : get all the educationalInstitutions.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationalInstitutions in body.
     */
    @GetMapping("/educational-institutions")
    public ResponseEntity<List<EducationalInstitution>> getAllEducationalInstitutions(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of EducationalInstitutions");
        Page<EducationalInstitution> page = educationalInstitutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /educational-institutions/:id} : get the "id" educationalInstitution.
     *
     * @param id the id of the educationalInstitution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationalInstitution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/educational-institutions/{id}")
    public ResponseEntity<EducationalInstitution> getEducationalInstitution(@PathVariable Long id) {
        log.debug("REST request to get EducationalInstitution : {}", id);
        Optional<EducationalInstitution> educationalInstitution = educationalInstitutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(educationalInstitution);
    }

    /**
     * {@code DELETE  /educational-institutions/:id} : delete the "id" educationalInstitution.
     *
     * @param id the id of the educationalInstitution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/educational-institutions/{id}")
    public ResponseEntity<Void> deleteEducationalInstitution(@PathVariable Long id) {
        log.debug("REST request to delete EducationalInstitution : {}", id);
        educationalInstitutionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
