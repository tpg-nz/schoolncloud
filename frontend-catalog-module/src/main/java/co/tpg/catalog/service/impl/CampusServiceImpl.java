package co.tpg.catalog.service.impl;

import co.tpg.catalog.service.CampusService;
import co.tpg.catalog.domain.Campus;
import co.tpg.catalog.repository.CampusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Campus}.
 */
@Service
@Transactional
public class CampusServiceImpl implements CampusService {

    private final Logger log = LoggerFactory.getLogger(CampusServiceImpl.class);

    private final CampusRepository campusRepository;

    public CampusServiceImpl(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
    }

    /**
     * Save a campus.
     *
     * @param campus the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Campus save(Campus campus) {
        log.debug("Request to save Campus : {}", campus);
        return campusRepository.save(campus);
    }

    /**
     * Get all the campuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Campus> findAll(Pageable pageable) {
        log.debug("Request to get all Campuses");
        return campusRepository.findAll(pageable);
    }


    /**
     * Get one campus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Campus> findOne(Long id) {
        log.debug("Request to get Campus : {}", id);
        return campusRepository.findById(id);
    }

    /**
     * Delete the campus by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Campus : {}", id);
        campusRepository.deleteById(id);
    }
}
