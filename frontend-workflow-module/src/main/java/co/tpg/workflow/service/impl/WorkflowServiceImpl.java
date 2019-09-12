package co.tpg.workflow.service.impl;

import co.tpg.workflow.service.WorkflowService;
import co.tpg.workflow.domain.Workflow;
import co.tpg.workflow.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Workflow}.
 */
@Service
@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    private final Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    private final WorkflowRepository workflowRepository;

    public WorkflowServiceImpl(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    /**
     * Save a workflow.
     *
     * @param workflow the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Workflow save(Workflow workflow) {
        log.debug("Request to save Workflow : {}", workflow);
        return workflowRepository.save(workflow);
    }

    /**
     * Get all the workflows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Workflow> findAll(Pageable pageable) {
        log.debug("Request to get all Workflows");
        return workflowRepository.findAll(pageable);
    }


    /**
     * Get one workflow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Workflow> findOne(Long id) {
        log.debug("Request to get Workflow : {}", id);
        return workflowRepository.findById(id);
    }

    /**
     * Delete the workflow by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workflow : {}", id);
        workflowRepository.deleteById(id);
    }
}
