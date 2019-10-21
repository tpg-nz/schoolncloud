package co.tpg.catalog.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.tpg.catalog.model.Address;

/**
 * Request class for Subject entity
 * 
 * @author maxx
 * @since 2019-10-11
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequest extends AbstractRequest<Address> {
    public AddressRequest() {
    }
}
