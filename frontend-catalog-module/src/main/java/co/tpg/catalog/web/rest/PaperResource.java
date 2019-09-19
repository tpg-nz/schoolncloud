package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.Paper;
import co.tpg.catalog.service.PaperService;
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
 * REST controller for managing {@link co.tpg.catalog.domain.Paper}.
 */
@RestController
@RequestMapping("/api")
public class PaperResource {

    private final Logger log = LoggerFactory.getLogger(PaperResource.class);

    private static final String ENTITY_NAME = "paper";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaperService paperService;

    public PaperResource(PaperService paperService) {
        this.paperService = paperService;
    }

    /**
     * {@code POST  /papers} : Create a new paper.
     *
     * @param paper the paper to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paper, or with status {@code 400 (Bad Request)} if the paper has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/papers")
    public ResponseEntity<Paper> createPaper(@Valid @RequestBody Paper paper) throws URISyntaxException {
        log.debug("REST request to save Paper : {}", paper);
        if (paper.getId() != null) {
            throw new BadRequestAlertException("A new paper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paper result = paperService.save(paper);
        return ResponseEntity.created(new URI("/api/papers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /papers} : Updates an existing paper.
     *
     * @param paper the paper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paper,
     * or with status {@code 400 (Bad Request)} if the paper is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/papers")
    public ResponseEntity<Paper> updatePaper(@Valid @RequestBody Paper paper) throws URISyntaxException {
        log.debug("REST request to update Paper : {}", paper);
        if (paper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Paper result = paperService.save(paper);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paper.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /papers} : get all the papers.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of papers in body.
     */
    @GetMapping("/papers")
    public ResponseEntity<List<Paper>> getAllPapers(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Papers");
        Page<Paper> page = paperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /papers/:id} : get the "id" paper.
     *
     * @param id the id of the paper to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paper, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/papers/{id}")
    public ResponseEntity<Paper> getPaper(@PathVariable Long id) {
        log.debug("REST request to get Paper : {}", id);
        Optional<Paper> paper = paperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paper);
    }

    /**
     * {@code DELETE  /papers/:id} : delete the "id" paper.
     *
     * @param id the id of the paper to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/papers/{id}")
    public ResponseEntity<Void> deletePaper(@PathVariable Long id) {
        log.debug("REST request to delete Paper : {}", id);
        paperService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
