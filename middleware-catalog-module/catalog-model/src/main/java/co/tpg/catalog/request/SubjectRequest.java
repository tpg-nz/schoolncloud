package co.tpg.catalog.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.tpg.catalog.model.Subject;

/**
 * Request class for Subject entity
 * 
 * @author maxx
 * @since 03-10-2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectRequest extends AbstractRequest<Subject> {
    public SubjectRequest() {
    }
}
