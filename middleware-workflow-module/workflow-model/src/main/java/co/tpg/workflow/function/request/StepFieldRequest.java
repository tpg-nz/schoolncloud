package co.tpg.workflow.function.request;

import co.tpg.workflow.function.model.StepField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Workflow step field request class
 * @author Andrej
 * @since 2019-10-10
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepFieldRequest  extends AbstractRequest<StepField> {
    public StepFieldRequest() {}
}
