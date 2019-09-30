package co.tpg.workflow.service.impl;

import co.tpg.workflow.service.StepService;
import co.tpg.workflow.domain.Step;
import co.tpg.workflow.repository.StepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Step}.
 */
@Service
@Transactional
public class StepServiceImpl implements StepService {

    private final Logger log = LoggerFactory.getLogger(StepServiceImpl.class);

    private final StepRepository stepRepository;

    public StepServiceImpl(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    /**
     * Save a step.
     *
     * @param step the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Step save(Step step) {
        log.debug("Request to save Step : {}", step);
        return stepRepository.save(step);
    }

    /**
     * Get all the steps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Step> findAll(Pageable pageable) {
        log.debug("Request to get all Steps");
        return stepRepository.findAll(pageable);
    }


    /**
     * Get one step by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Step> findOne(Long id) {
        log.debug("Request to get Step : {}", id);
        return stepRepository.findById(id);
    }

    /**
     * Delete the step by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Step : {}", id);
        stepRepository.deleteById(id);
    }
}
