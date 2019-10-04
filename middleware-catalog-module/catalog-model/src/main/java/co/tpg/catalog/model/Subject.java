package co.tpg.catalog.model;

import lombok.*;

/**
 * Response class for Subject entity
 * @author maxx
 * @since 2019-10-03
 */


@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Subject extends AbstractModel<String> {
    private String id;
    private String name;
    private Subject overview;
    private int level;

    public Subject() {
    }

    public Subject(String id, String name, Subject overview, int level) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.level = level;
    }

    public String getOverviewId() {
        return this.overview.getId();
    }

}