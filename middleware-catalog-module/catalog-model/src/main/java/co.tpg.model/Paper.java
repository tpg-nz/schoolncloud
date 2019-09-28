package co.tpg.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Paper extends AbstractModel<String> {
    private String id;
}
