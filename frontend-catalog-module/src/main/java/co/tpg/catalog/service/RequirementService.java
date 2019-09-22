package co.tpg.catalog.service;

import co.tpg.catalog.domain.Requirement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Requirement}.
 */
public interface RequirementService {

    /**
     * Save a requirement.
     *
     * @param requirement the entity to save.
     * @return the persisted entity.
     */
    Requirement save(Requirement requirement);

    /**
     * Get all the requirements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Requirement> findAll(Pageable pageable);


    /**
     * Get the "id" requirement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Requirement> findOne(Long id);

    /**
     * Delete the "id" requirement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
