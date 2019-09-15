package co.tpg.catalog.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import co.tpg.catalog.domain.enumeration.ContactType;

import co.tpg.catalog.domain.enumeration.MediaTypee;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private String guid;

    @Column(name = "entity_guid")
    private String entityGuid;

    @Column(name = "contact")
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type")
    private ContactType contactType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaTypee mediaType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public Contact guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getEntityGuid() {
        return entityGuid;
    }

    public Contact entityGuid(String entityGuid) {
        this.entityGuid = entityGuid;
        return this;
    }

    public void setEntityGuid(String entityGuid) {
        this.entityGuid = entityGuid;
    }

    public String getContact() {
        return contact;
    }

    public Contact contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public Contact contactType(ContactType contactType) {
        this.contactType = contactType;
        return this;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public MediaTypee getMediaType() {
        return mediaType;
    }

    public Contact mediaType(MediaTypee mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public void setMediaType(MediaTypee mediaType) {
        this.mediaType = mediaType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", entityGuid='" + getEntityGuid() + "'" +
            ", contact='" + getContact() + "'" +
            ", contactType='" + getContactType() + "'" +
            ", mediaType='" + getMediaType() + "'" +
            "}";
    }
}
