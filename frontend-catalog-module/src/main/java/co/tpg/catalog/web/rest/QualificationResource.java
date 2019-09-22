package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.Qualification;
import co.tpg.catalog.service.QualificationService;
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
 * REST controller for managing {@link co.tpg.catalog.domain.Qualification}.
 */
@RestController
@RequestMapping("/api")
public class QualificationResource {

    private final Logger log = LoggerFactory.getLogger(QualificationResource.class);

    private static final String ENTITY_NAME = "qualification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QualificationService qualificationService;

    public QualificationResource(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    /**
     * {@code POST  /qualifications} : Create a new qualification.
     *
     * @param qualification the qualification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qualification, or with status {@code 400 (Bad Request)} if the qualification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/qualifications")
    public ResponseEntity<Qualification> createQualification(@Valid @RequestBody Qualification qualification) throws URISyntaxException {
        log.debug("REST request to save Qualification : {}", qualification);
        if (qualification.getId() != null) {
            throw new BadRequestAlertException("A new qualification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Qualification result = qualificationService.save(qualification);
        return ResponseEntity.created(new URI("/api/qualifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qualifications} : Updates an existing qualification.
     *
     * @param qualification the qualification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qualification,
     * or with status {@code 400 (Bad Request)} if the qualification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qualification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/qualifications")
    public ResponseEntity<Qualification> updateQualification(@Valid @RequestBody Qualification qualification) throws URISyntaxException {
        log.debug("REST request to update Qualification : {}", qualification);
        if (qualification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Qualification result = qualificationService.save(qualification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qualification.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /qualifications} : get all the qualifications.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qualifications in body.
     */
    @GetMapping("/qualifications")
    public ResponseEntity<List<Qualification>> getAllQualifications(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Qualifications");
        Page<Qualification> page = qualificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qualifications/:id} : get the "id" qualification.
     *
     * @param id the id of the qualification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qualification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/qualifications/{id}")
    public ResponseEntity<Qualification> getQualification(@PathVariable Long id) {
        log.debug("REST request to get Qualification : {}", id);
        Optional<Qualification> qualification = qualificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qualification);
    }

    /**
     * {@code DELETE  /qualifications/:id} : delete the "id" qualification.
     *
     * @param id the id of the qualification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/qualifications/{id}")
    public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
        log.debug("REST request to delete Qualification : {}", id);
        qualificationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
