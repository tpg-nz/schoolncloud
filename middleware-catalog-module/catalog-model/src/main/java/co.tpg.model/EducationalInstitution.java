package co.tpg.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class to represent a Educational Institution.
 * @author Pamela
 * @since 2019-09-18
 */
@Builder
@Getter
@Setter
@ToString
public class EducationalInstitution extends AbstractModel<String> {
    private String id;
    private String name;

    public EducationalInstitution() {
    }

    public EducationalInstitution(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
