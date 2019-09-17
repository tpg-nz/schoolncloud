package co.tpg.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class to represent a Subject
 * @author James
 * @since 2019-09-16
 */

@Builder
@Getter
@Setter
@ToString
public class Subject {
    private String guid;
    private String name;
    private String overview;
    private int level;

    public Subject() {
    }

    public Subject(String guid, String name, String overview, int level) {
        this.guid = guid;
        this.name = name;
        this.overview = overview;
        this.level = level;
    }
}
