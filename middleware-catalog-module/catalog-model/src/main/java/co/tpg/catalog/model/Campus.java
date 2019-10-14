package co.tpg.catalog.model;

import lombok.*;

/**
 * Model class to represent Campuses.
 * @author Rod
 * @since 2019-09-17
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Campus extends AbstractModel<String> {
    private String id;
    private String name;

    public Campus() {
    }

    public Campus(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
