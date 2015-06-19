package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.domain.Event;
import edu.usm.domain.Views;
import edu.usm.dto.Response;
import edu.usm.mapper.ContactDtoMapper;
import edu.usm.mapper.ContactMapper;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by scottkimball on 3/12/15.
 */

@RestController
@RequestMapping(value = "/contacts" )
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private ContactDtoMapper dtoMapper;

    private Logger logger = LoggerFactory.getLogger(ContactController.class);


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getContacts() {
        return contactService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
    public Response createContact(@RequestBody Contact contact) {
        logger.debug("POST request to /contacts");
        String id;
        try {
            id = (contactService.create(contact));

        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Unable to create contact");
        }

        return new Response(id,Response.SUCCESS,null);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.ContactDetails.class)
    public Contact getContactById(@PathVariable("id") String id) {
        logger.debug("GET request to /contacts/contact/"+id);
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes={"application/json"})
    public Response updateContactById(@PathVariable("id") String id, @RequestBody Contact contact) {
        logger.debug("PUT request to /contacts/contact/"+id);

        try {
            contactService.update(contact);
            return new Response(contact.getId(), Response.SUCCESS, null);

        } catch (Exception e) {
            logger.error(e.toString());
            return new Response(contact.getId(),Response.FAILURE,"Unable to update contact");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/attend")
    public void attendEvent(@PathVariable("id") String id, @RequestBody Event event) {
        logger.debug("POST to /contacts/"+id+"/attend");
        Contact contact = contactService.findById(id);
        event = eventService.findById(event.getId());

        if (!contact.getAttendedEvents().contains(event)) {
            contact.getAttendedEvents().add(event);
            contactService.update(contact);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/encounters")
    @JsonView(Views.ContactEncounterDetails.class)
    public List<Encounter> getAllEncountersForContact(@PathVariable("id") String id) {
        return contactService.findById(id).getEncounters();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/initiators")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getAllInitiators() {
        return contactService.findAllInitiators();
    }

}

