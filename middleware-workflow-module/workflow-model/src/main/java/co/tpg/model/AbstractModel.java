package co.tpg.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Top level model class.
 * @author Rod
 * @since 2019-09-04
 */
public abstract class AbstractModel<T> implements Domain<T>, Serializable {
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Domain<T> that = (Domain<T>) o;
        return Objects.equals(getId(),that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
