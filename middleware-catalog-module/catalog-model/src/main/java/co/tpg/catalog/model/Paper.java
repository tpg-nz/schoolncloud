package co.tpg.catalog.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Paper extends AbstractModel<String> {
    private String id;

    public Paper() {
    }

    public Paper(String id) {
        this.id = id;
    }
}
