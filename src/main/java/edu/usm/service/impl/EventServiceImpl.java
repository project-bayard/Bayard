package edu.usm.service.impl;

import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DonationDto;
import edu.usm.dto.EventDto;
import edu.usm.repository.EventDao;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */

@Service
public class EventServiceImpl extends DonationAssigningService implements EventService {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CommitteeService committeeService;

    private Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Override
    public Set<Event> findAll() {
        return (Set<Event>) eventDao.findAll();
    }

    @Override
    @Transactional
    public Event findById(String id) throws NullDomainReference.NullEvent {
        if (null == id) {
            return null;
        }
        Event event = findEvent(id);

        if (event.getGroups() == null) {
            event.setGroups(new HashSet<>());
        }

        if (event.getAttendees() == null) {
            event.setAttendees(new HashSet<>());
        }

        if (event.getCommittee() != null) {
            event.getCommittee().getName();
        }

        if (event.getDonations() == null) {
            event.setDonations(new HashSet<>());
        }

        event.getDonations().size();
        event.getAttendees().size();
        event.getGroups().size();
        return eventDao.findOne(id);
    }

    @Override
    public String create(EventDto dto, String committeeId) throws ConstraintViolation, NullDomainReference {
        Committee committee = committeeService.findById(committeeId);
        Event event = new Event();
        event.setName(dto.getName());
        event.setNotes(dto.getNotes());
        event.setLocation(dto.getLocation());
        event.setDateHeld(dto.getDateHeld());
        event.setCommittee(committee);
        return create(event);
    }

    @Override
    public String create(Event event) throws ConstraintViolation, NullDomainReference {
        validateEvent(event);
        eventDao.save(event);
        return event.getId();
    }

    @Override
    public Set<Event> findByName(String name) {
        return eventDao.findByName(name);
    }

    private void validateEvent(Event event) throws ConstraintViolation, NullDomainReference {

        if (null == event) {
            throw new NullDomainReference.NullEvent();
        }

        if (null == event.getName() || event.getName().isEmpty()) {
            throw new ConstraintViolation(ConstraintMessage.EVENT_REQUIRED_NAME);
        }

        if (null == event.getDateHeld() || event.getDateHeld().isEmpty()) {
            throw new ConstraintViolation(ConstraintMessage.EVENT_REQUIRED_DATE);
        }

        Set<Event> sharedName = findByName(event.getName());
        if (null != sharedName) {
            Iterator<Event> iterator = sharedName.iterator();
            while (iterator.hasNext()) {
                Event namedEvent = iterator.next();
                boolean sameEvent = event.getId() != null && event.getId().equalsIgnoreCase(namedEvent.getId());
                if (event.getDateHeld().equalsIgnoreCase(namedEvent.getDateHeld()) && !sameEvent) {
                    throw new ConstraintViolation(ConstraintMessage.EVENT_NON_UNIQUE);
                }
            }
        }
    }


    @Override
    @Transactional
    public void delete(String id) throws ConstraintViolation, NullDomainReference {
        Event event = eventDao.findOne(id);
        if (null ==  event) {
            throw new NullDomainReference.NullEvent();
        }

        Set<Contact> attendees = new HashSet<>(event.getAttendees());

        for (Contact contact : attendees) {
            contactService.unattendEvent(contact.getId(),event.getId());
        }
        eventDao.delete(event);
    }

    @Override
    @Transactional
    public void deleteAll() {
        logger.debug("Deleting all events!");
        Set<Event> events = findAll();
        events.stream().forEach(this::uncheckedDelete);
    }


    @Override
    public void update(String eventId, EventDto eventDto) throws ConstraintViolation, NullDomainReference {
        Event event = findEvent(eventId);
        if (eventDto.getCommitteeId() != null && !eventDto.getCommitteeId().isEmpty()) {
            event.setCommittee(committeeService.findById(eventDto.getCommitteeId()));
        } else {
            event.setCommittee(null);
        }

        event.setNotes(eventDto.getNotes());
        event.setLocation(eventDto.getLocation());
        event.setDateHeld(eventDto.getDateHeld());
        event.setName(eventDto.getName());
        update(event);
    }


    @Override
    @Transactional
    public void addDonation(String id, DonationDto dto) throws ConstraintViolation, NullDomainReference {
        Donation donation = convertToDonation(dto);
        Event event = eventDao.findOne(id);
        event.addDonation(donation);
        updateLastModified(donation);
        update(event);
    }

    @Override
    @Transactional
    public void removeDonation(String eventId, String donationId) throws ConstraintViolation, NullDomainReference {
        Donation donation = donationService.findById(donationId);
        Event event = findEvent(eventId);

        if (donation == null) {
            throw new NullDomainReference.NullDonation(donationId);
        }
        if (null != event.getDonations()) {
            event.getDonations().remove(donation);
            updateLastModified(donation);
            update(event);
        }
    }

    @Override
    @Transactional
    public Set<Group> getAllEventGroups(String eventId) throws NullDomainReference {
        Event event = findById(eventId);
        Set<Group> groups = event.getGroups();
        groups.size();
        return groups;
    }

    public Event findEventWithDonation(Donation donation) {
        return eventDao.findByDonations_id(donation.getId());
    }

    @Override
    @Transactional
    public Committee getEventCommittee(String eventId) throws NullDomainReference {
        Event event = findEvent(eventId);
        Committee committee = event.getCommittee();
        if (committee != null) {
            committee.getName();
        }
        return committee;
    }

    @Override
    @Transactional
    public Set<Contact> getAllAttendees(String eventId) throws NullDomainReference {
        Event event = findEvent(eventId);
        Set<Contact> attendees = event.getAttendees();

        if (attendees == null) {
            attendees = new HashSet<>();
        }

        attendees.size();
        return attendees;
    }

    @Override
    @Transactional
    public void removeContactFromEvent(String contactId, String eventID) throws NullDomainReference {
        Event event = findEvent(eventID);
        Contact contact = contactService.findById(contactId);

        if (contact == null) {
            throw new NullDomainReference.NullContact(contactId);
        }

        if (event.getAttendees() != null) {
            Set<Contact> attendees = new HashSet<>(event.getAttendees());
            attendees.remove(contact);
            event.setAttendees(attendees);
            eventDao.save(event);
        }
    }

    @Override
    public void removeCommitteeFromEvent(String eventId) throws NullDomainReference, ConstraintViolation {
        Event event = findEvent(eventId);
        event.setCommittee(null);
        update(event);
    }

    @Override
    @Transactional
    public Set<Donation> getAllEventDonations(String eventId) throws NullDomainReference {
        Event event = findEvent(eventId);
        Set<Donation> donations = event.getDonations();

        if (donations == null) {
            donations = new HashSet<>();
        }
        donations.size();
        return donations;
    }

    private Event findEvent(String id) throws NullDomainReference.NullEvent {
        Event event = eventDao.findOne(id);

        if (event == null) {
            throw new NullDomainReference.NullEvent(id);
        }
        return event;
    }

    private void update(Event event) throws ConstraintViolation, NullDomainReference {
        validateEvent(event);
        updateLastModified(event);
        eventDao.save(event);
    }

}
