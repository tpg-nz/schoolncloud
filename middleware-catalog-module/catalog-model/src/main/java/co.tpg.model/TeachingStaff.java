package co.tpg.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class to represent a Teaching Staff.
 * @author Rod
 * @since 2019-09-04
 */
@Builder
@Getter
@Setter
@ToString
public class TeachingStaff extends AbstractModel<String> {
    private String id;
    private String name;

    public TeachingStaff() {
    }

    public TeachingStaff(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "TeachingStaff{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
