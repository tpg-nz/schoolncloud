package co.tpg.catalog.service.impl;

import co.tpg.catalog.service.RequirementService;
import co.tpg.catalog.domain.Requirement;
import co.tpg.catalog.repository.RequirementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Requirement}.
 */
@Service
@Transactional
public class RequirementServiceImpl implements RequirementService {

    private final Logger log = LoggerFactory.getLogger(RequirementServiceImpl.class);

    private final RequirementRepository requirementRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    /**
     * Save a requirement.
     *
     * @param requirement the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Requirement save(Requirement requirement) {
        log.debug("Request to save Requirement : {}", requirement);
        return requirementRepository.save(requirement);
    }

    /**
     * Get all the requirements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Requirement> findAll(Pageable pageable) {
        log.debug("Request to get all Requirements");
        return requirementRepository.findAll(pageable);
    }


    /**
     * Get one requirement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Requirement> findOne(Long id) {
        log.debug("Request to get Requirement : {}", id);
        return requirementRepository.findById(id);
    }

    /**
     * Delete the requirement by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Requirement : {}", id);
        requirementRepository.deleteById(id);
    }
}
