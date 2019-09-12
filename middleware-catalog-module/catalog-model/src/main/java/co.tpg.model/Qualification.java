package co.tpg.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class to represent a Qualification
 * @author James
 * @since 2019-09-11
 */

@Builder
@Getter
@Setter
@ToString

public class Qualification extends AbstractModel<Stirng> {
    private String id;
    private String name;
    private String hyperlink;
    private Subject subject;

    public Qualification(String id, String name, String hyperlink) {
        this.id = id;
        this.name = name;
        this.hyperlink = hyperlink;
    }

    public String toString() {
        return "Qualification{" +
                "id='" + this.id + "'" + ", name='" +
                this.name + "'" + ", hyperlink='" + this.hyperlink +
                "'" + "}";
    }
}