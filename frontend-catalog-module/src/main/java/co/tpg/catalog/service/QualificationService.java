package co.tpg.catalog.service;

import co.tpg.catalog.domain.Qualification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Qualification}.
 */
public interface QualificationService {

    /**
     * Save a qualification.
     *
     * @param qualification the entity to save.
     * @return the persisted entity.
     */
    Qualification save(Qualification qualification);

    /**
     * Get all the qualifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Qualification> findAll(Pageable pageable);


    /**
     * Get the "id" qualification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Qualification> findOne(Long id);

    /**
     * Delete the "id" qualification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
