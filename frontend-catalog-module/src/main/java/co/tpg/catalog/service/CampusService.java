package co.tpg.catalog.service;

import co.tpg.catalog.domain.Campus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Campus}.
 */
public interface CampusService {

    /**
     * Save a campus.
     *
     * @param campus the entity to save.
     * @return the persisted entity.
     */
    Campus save(Campus campus);

    /**
     * Get all the campuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Campus> findAll(Pageable pageable);


    /**
     * Get the "id" campus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Campus> findOne(Long id);

    /**
     * Delete the "id" campus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
