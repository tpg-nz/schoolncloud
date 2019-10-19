package co.tpg.workflow.function.request;

import co.tpg.workflow.function.model.Step;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Workflow step request class
 * @author Andrej
 * @since 2019-10-10
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepRequest extends AbstractRequest<Step> {
    public StepRequest() { }
}
