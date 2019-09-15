package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.UserRole;
import co.tpg.catalog.repository.UserRoleRepository;
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
 * REST controller for managing {@link co.tpg.catalog.domain.UserRole}.
 */
@RestController
@RequestMapping("/api")
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "userRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRoleRepository userRoleRepository;

    public UserRoleResource(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * {@code POST  /user-roles} : Create a new userRole.
     *
     * @param userRole the userRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRole, or with status {@code 400 (Bad Request)} if the userRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-roles")
    public ResponseEntity<UserRole> createUserRole(@Valid @RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", userRole);
        if (userRole.getId() != null) {
            throw new BadRequestAlertException("A new userRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRole result = userRoleRepository.save(userRole);
        return ResponseEntity.created(new URI("/api/user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-roles} : Updates an existing userRole.
     *
     * @param userRole the userRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRole,
     * or with status {@code 400 (Bad Request)} if the userRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-roles")
    public ResponseEntity<UserRole> updateUserRole(@Valid @RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to update UserRole : {}", userRole);
        if (userRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserRole result = userRoleRepository.save(userRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-roles} : get all the userRoles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRoles in body.
     */
    @GetMapping("/user-roles")
    public List<UserRole> getAllUserRoles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UserRoles");
        return userRoleRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /user-roles/:id} : get the "id" userRole.
     *
     * @param id the id of the userRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-roles/{id}")
    public ResponseEntity<UserRole> getUserRole(@PathVariable Long id) {
        log.debug("REST request to get UserRole : {}", id);
        Optional<UserRole> userRole = userRoleRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(userRole);
    }

    /**
     * {@code DELETE  /user-roles/:id} : delete the "id" userRole.
     *
     * @param id the id of the userRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-roles/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
