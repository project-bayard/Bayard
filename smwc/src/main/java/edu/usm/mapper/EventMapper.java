package edu.usm.mapper;

import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.dto.EventDto;
import edu.usm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andrew on 5/31/2015.
 */
@Component
@Scope("singleton")
public class EventMapper {

    @Autowired
    private ContactService contactService;

    public Event fromDto(EventDto dto) {

        Event event = (null == dto.getId()) ? new Event() : new Event(dto.getId());
        event.setName(dto.getName());
        event.setNotes(dto.getNotes());
        event.setLocation(dto.getLocation());
        event.setDateHeld(dto.getDateHeld());

        if (null != dto.getAttendees()) {
            Set<Contact> attendees = new HashSet<>();
            Iterator<String> contactIds = dto.getAttendees().iterator();
            while(contactIds.hasNext()) {
                Contact contact = contactService.findById(contactIds.next());
                attendees.add(contact);
            }
            event.setAttendees(attendees);
        }

        return event;

    }

    public EventDto fromEvent(Event event) {
        return null;
    }

}
