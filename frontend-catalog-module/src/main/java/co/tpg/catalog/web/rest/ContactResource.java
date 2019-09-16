package co.tpg.catalog.web.rest;

import co.tpg.catalog.domain.Contact;
import co.tpg.catalog.repository.ContactRepository;
import co.tpg.catalog.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link co.tpg.catalog.domain.Contact}.
 */
@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private static final String ENTITY_NAME = "contact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactRepository contactRepository;

    public ContactResource(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * {@code POST  /contacts} : Create a new contact.
     *
     * @param contact the contact to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contact, or with status {@code 400 (Bad Request)} if the contact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contacts")
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contact);
        if (contact.getId() != null) {
            throw new BadRequestAlertException("A new contact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contact result = contactRepository.save(contact);
        return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contacts} : Updates an existing contact.
     *
     * @param contact the contact to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contact,
     * or with status {@code 400 (Bad Request)} if the contact is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contact couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contacts")
    public ResponseEntity<Contact> updateContact(@Valid @RequestBody Contact contact) throws URISyntaxException {
        log.debug("REST request to update Contact : {}", contact);
        if (contact.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Contact result = contactRepository.save(contact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contact.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contacts} : get all the contacts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contacts in body.
     */
    @GetMapping("/contacts")
    public List<Contact> getAllContacts() {
        log.debug("REST request to get all Contacts");
        return contactRepository.findAll();
    }

    /**
     * {@code GET  /contacts/:id} : get the "id" contact.
     *
     * @param id the id of the contact to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contact, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contacts/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        Optional<Contact> contact = contactRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contact);
    }

    /**
     * {@code DELETE  /contacts/:id} : delete the "id" contact.
     *
     * @param id the id of the contact to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        contactRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
