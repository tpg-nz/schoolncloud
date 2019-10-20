package co.tpg.workflow.function.request;

import co.tpg.workflow.function.model.Workflow;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Workflow request class
 * @author Andrej
 * @since 2019-10-10
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowRequest extends AbstractRequest<Workflow> {
    public WorkflowRequest() { }
}
