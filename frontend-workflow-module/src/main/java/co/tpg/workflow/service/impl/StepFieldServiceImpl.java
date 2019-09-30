package co.tpg.workflow.service.impl;

import co.tpg.workflow.service.StepFieldService;
import co.tpg.workflow.domain.StepField;
import co.tpg.workflow.repository.StepFieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link StepField}.
 */
@Service
@Transactional
public class StepFieldServiceImpl implements StepFieldService {

    private final Logger log = LoggerFactory.getLogger(StepFieldServiceImpl.class);

    private final StepFieldRepository stepFieldRepository;

    public StepFieldServiceImpl(StepFieldRepository stepFieldRepository) {
        this.stepFieldRepository = stepFieldRepository;
    }

    /**
     * Save a stepField.
     *
     * @param stepField the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StepField save(StepField stepField) {
        log.debug("Request to save StepField : {}", stepField);
        return stepFieldRepository.save(stepField);
    }

    /**
     * Get all the stepFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StepField> findAll(Pageable pageable) {
        log.debug("Request to get all StepFields");
        return stepFieldRepository.findAll(pageable);
    }


    /**
     * Get one stepField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StepField> findOne(Long id) {
        log.debug("Request to get StepField : {}", id);
        return stepFieldRepository.findById(id);
    }

    /**
     * Delete the stepField by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StepField : {}", id);
        stepFieldRepository.deleteById(id);
    }
}
