package edu.usm.service;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Event;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EventDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Event findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Event> findByName(String name);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create (Event event) throws ConstraintViolation, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create (EventDto dto, Committee committee) throws ConstraintViolation, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Event> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete(Event event) throws ConstraintViolation, NullDomainReference.NullEvent, NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Event event) throws ConstraintViolation, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Event event, EventDto eventDto) throws ConstraintViolation, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addDonation(Event event, Donation donation) throws ConstraintViolation, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeDonation(Event event, Donation donation) throws ConstraintViolation, NullDomainReference.NullEvent;
}
