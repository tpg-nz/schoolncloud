package co.tpg.catalog.service;

import co.tpg.catalog.domain.Paper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Paper}.
 */
public interface PaperService {

    /**
     * Save a paper.
     *
     * @param paper the entity to save.
     * @return the persisted entity.
     */
    Paper save(Paper paper);

    /**
     * Get all the papers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Paper> findAll(Pageable pageable);


    /**
     * Get the "id" paper.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Paper> findOne(Long id);

    /**
     * Delete the "id" paper.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
