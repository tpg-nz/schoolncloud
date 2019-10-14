package co.tpg.catalog.model;

import lombok.*;

/**
 * Model class to represent a Teaching Staff.
 * @author Rod
 * @since 2019-09-04
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TeachingStaff extends AbstractModel<String> {
    private String id;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private Paper paper;

    public TeachingStaff() {
    }

    public TeachingStaff(String id, String name, Paper paper) {
        this.id = id;
        this.name = name;
        this.paper = paper;
    }
}
