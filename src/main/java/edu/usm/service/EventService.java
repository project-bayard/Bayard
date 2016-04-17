package edu.usm.service;

import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DonationDto;
import edu.usm.dto.EventDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Event findById(String id) throws NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Event> findByName(String name);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create (Event event) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create (EventDto dto, String committeeId) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Event> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete(String id) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();


    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(String eventId, EventDto eventDto) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addDonation(String id, DonationDto dto) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeDonation(String eventId, String donationId) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Group> getAllEventGroups(String eventId) throws NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Event findEventWithDonation(Donation donation);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Committee getEventCommittee (String eventId) throws NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Contact> getAllAttendees(String eventId) throws NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeContactFromEvent(String contactId, String eventID) throws  NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeCommitteeFromEvent(String eventId) throws NullDomainReference, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Donation> getAllEventDonations(String eventId) throws NullDomainReference;
}
