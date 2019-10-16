package co.tpg.catalog.service;

import co.tpg.catalog.domain.TeachingClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TeachingClass}.
 */
public interface TeachingClassService {

    /**
     * Save a teachingClass.
     *
     * @param teachingClass the entity to save.
     * @return the persisted entity.
     */
    TeachingClass save(TeachingClass teachingClass);

    /**
     * Get all the teachingClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TeachingClass> findAll(Pageable pageable);


    /**
     * Get the "id" teachingClass.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeachingClass> findOne(Long id);

    /**
     * Delete the "id" teachingClass.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
