package edu.usm.service.impl;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EventDto;
import edu.usm.repository.EventDao;
import edu.usm.service.BasicService;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */

@Service
public class EventServiceImpl extends BasicService implements EventService {

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
    public Event findById(String id) {
        if (null == id) {
            return null;
        }
        return eventDao.findOne(id);
    }

    @Override
    public String create(EventDto dto, Committee committee) throws ConstraintViolation, NullDomainReference.NullEvent {
        Event event = new Event();
        event.setName(dto.getName());
        event.setNotes(dto.getNotes());
        event.setLocation(dto.getLocation());
        event.setDateHeld(dto.getDateHeld());
        event.setCommittee(committee);
        return create(event);
    }

    @Override
    public String create(Event event) throws ConstraintViolation, NullDomainReference.NullEvent {
        validateEvent(event);
        eventDao.save(event);
        return event.getId();
    }

    private void validateEvent(Event event) throws ConstraintViolation, NullDomainReference.NullEvent {

        if (null == event) {
            throw new NullDomainReference.NullEvent();
        }

        if (null == event.getName()) {
            throw new ConstraintViolation(ConstraintMessage.EVENT_REQUIRED_NAME);
        }

        if (null == event.getDateHeld()) {
            throw new ConstraintViolation(ConstraintMessage.EVENT_REQUIRED_DATE);
        }
    }

    private void uncheckedDelete(Event event) {
        try {
            delete(event);
        } catch (NullDomainReference e) {
            throw new RuntimeException(e);
        } catch (ConstraintViolation e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Event event) throws ConstraintViolation, NullDomainReference.NullEvent, NullDomainReference.NullContact {

        if (null ==  event) {
            throw new NullDomainReference.NullEvent();
        }

        Set<Contact> attendees = new HashSet<>(event.getAttendees());

        for (Contact contact : attendees) {
            contactService.unattendEvent(contact,event);
        }
        eventDao.delete(event);
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all events!");
        Set<Event> events = findAll();
        events.stream().forEach(this::uncheckedDelete);
    }

    @Override
    public void update(Event event) throws ConstraintViolation, NullDomainReference.NullEvent{
        validateEvent(event);
        updateLastModified(event);
        eventDao.save(event);
    }

    @Override
    public void update(Event event, EventDto eventDto) throws ConstraintViolation, NullDomainReference.NullEvent{

        if (eventDto.getCommitteeId() != null && !eventDto.getCommitteeId().isEmpty()) {
            event.setCommittee(committeeService.findById(eventDto.getCommitteeId()));
        }
        event.setNotes(eventDto.getNotes());
        event.setLocation(eventDto.getLocation());
        event.setDateHeld(eventDto.getDateHeld());
        event.setName(eventDto.getName());
        update(event);
    }
}
