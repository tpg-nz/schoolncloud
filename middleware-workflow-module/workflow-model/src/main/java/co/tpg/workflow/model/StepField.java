package co.tpg.workflow.model;

import lombok.*;

/**
 * Model class to represents a workflow step field.
 * @author Andrej
 * @since 2019-10-08
 */

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class StepField extends AbstractModel<String> implements Cloneable {

    private String label;
    private String id;
    private int sequence;

    /**
     * Main constructor
     * @param id        Workflow step field UUID
     * @param label     Workflow step field label
     * @param sequence  Workflow step field sequence
     */
    public StepField(String id, String label, int sequence) {
        this.id = id;
        this.label = label;
        this.sequence = sequence;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
