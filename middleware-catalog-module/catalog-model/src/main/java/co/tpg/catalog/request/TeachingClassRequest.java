package co.tpg.catalog.request;

import co.tpg.catalog.model.TeachingClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request class for TeachingClass (Classroom) model.
 * @author Rod
 * @since 2019-09-28
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeachingClassRequest extends AbstractRequest<TeachingClass> {
    public TeachingClassRequest() {
    }
}
