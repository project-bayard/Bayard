package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.*;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.IdDto;
import edu.usm.dto.Response;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private OrganizationService organizationService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private EncounterService encounterService;

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
            return new Response(id,Response.SUCCESS,null);
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Unable to create contact");
        }
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
    public Response updateContactById(@PathVariable("id") String id, @RequestBody Contact details) {
        logger.debug("PUT request to /contacts/contact/"+id);

        Contact contact = contactService.findById(id);

        if (contact == null) {
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.updateBasicDetails(contact, details);
            return new Response(id, Response.SUCCESS, null);

        } catch (Exception e) {
            logger.error(e.toString());
            return new Response(contact.getId(),Response.FAILURE,"Unable to update contact");
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}/events")
    @JsonView(Views.EventList.class)
    public Set<Event> getAttendedEvents(@PathVariable("id") String id) {
        Contact contact = contactService.findById(id);
        return contact.getAttendedEvents();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/events")
    public Response attendEvent(@PathVariable("id") String id, @RequestBody IdDto eventIdDto) {
        logger.debug("POST to /contacts/"+id+"/attend");
        Contact contact = contactService.findById(id);

        if (null == contact) {
            return Response.failNonexistentContact(id);
        }

        Event event = eventService.findById(eventIdDto.getId());
        if (null == event) {
            return new Response(null, Response.FAILURE, "Event with ID "+eventIdDto.getId()+" does not exist");
        }

        if (null == contact.getAttendedEvents()) {
            contact.setAttendedEvents(new HashSet<>());
        }

        try {
            contactService.attendEvent(contact, event);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Contact with Event");
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/encounters")
    @JsonView(Views.ContactEncounterDetails.class)
    public List<Encounter> getAllEncountersForContact(@PathVariable("id") String id) {
        return contactService.findById(id).getEncounters();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters/{encounterId}")
    public Response updateEncounter(@PathVariable("id") String id, @PathVariable("encounterId") String encounterId, @RequestBody EncounterDto encounterDto) {
        Contact contact = contactService.findById(id);
        Contact initiator = contactService.findById(encounterDto.getInitiatorId());
        Encounter encounter = encounterService.findById(encounterId);
        if (null == contact) {
            return Response.failNonexistentContact(id);
        }

        if (null == encounter) {
            return new Response(null, Response.FAILURE, "Encounter with ID "+encounterId+" does not exist");
        }

        if (null == initiator) {
            return new Response(null, Response.FAILURE, "Initiator with ID "+id+" does not exist");
        }

        if (!contact.getId().equals(encounter.getContact().getId())) {
            return new Response(null, Response.FAILURE, "Discrepancy between Contact with ID "+id+" and Contact("+encounter.getContact().getId()+") associated with this Encounter. Unable to update.");
        }

        try {
            encounterService.updateEncounter(encounter, encounterDto);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Encounter");
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters")
    public Response createEncounter(@PathVariable("id") String id, @RequestBody EncounterDto encounterDto) {
        Contact contact = contactService.findById(id);

        if (null == contact) {
            return Response.failNonexistentContact(id);
        }

        Contact initiator = contactService.findById(encounterDto.getInitiatorId());
        if (null == initiator) {
            return new Response(null, Response.FAILURE, "Initiator with ID "+encounterDto.getInitiatorId()+" does not exist");
        }

        try {
            contactService.addEncounter(contact, initiator, encounterDto);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Contact with new encounter");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/initiators")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getAllInitiators() {
        return contactService.findAllInitiators();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/organizations")
    @JsonView(Views.ContactOrganizationDetails.class)
    public Set<Organization> getAllOrganizationsForContact(@PathVariable("id") String id) {
        return contactService.findById(id).getOrganizations();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/organizations", consumes= {"application/json"})
    public Response addContactToOrganization(@PathVariable("id") String id, @RequestBody IdDto idDto) {

        String idStringed = idDto.getId();

        Organization organization = organizationService.findById(idStringed);
        Contact contact = contactService.findById(id);

        if (organization == null) {
            logger.debug("No org");
            return new Response(null,Response.FAILURE, "Organization with ID " + idStringed + " does not exist");

        } else if (contact == null) {
            logger.debug("No contact");
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.addContactToOrganization(contact,organization);
            return Response.successGeneric();
        } catch (Exception e) {
            logger.debug("Bad service call");
            return new Response(null, Response.FAILURE, "Unable to add contact with ID " + contact.getId() +
                    " to organization with ID " + organization.getId());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/organizations/{orgId}", consumes= {"application/json"})
    public Response removeContactFromOrganization(@PathVariable("id") String id, @PathVariable("orgId") String orgId) {

        Organization organization = organizationService.findById(orgId);
        Contact contact = contactService.findById(id);

        if (organization == null) {
            logger.debug("No org");
            return new Response(null,Response.FAILURE, "Organization with ID " + orgId + " does not exist");

        } else if (contact == null) {
            logger.debug("No contact");
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.removeContactFromOrganization(contact,organization);
            return Response.successGeneric();

        } catch (Exception e) {
            logger.debug("Bad service call");
            return new Response(null, Response.FAILURE, "Unable to remove contact with ID " + contact.getId() +
                    " from organization with ID " + organization.getId());
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/memberinfo")
    @JsonView(Views.MemberInfo.class)
    public MemberInfo getMemberInfo(@PathVariable("id") String id) {
        Contact contact = contactService.findById(id);
        return (null == contact) ? null : contact.getMemberInfo();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/memberinfo")
    public Response updateMemberInfo(@PathVariable("id") String id, @RequestBody MemberInfo memberInfo) {
        Contact contact = contactService.findById(id);
        if (null == contact) {
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.updateMemberInfo(contact, memberInfo);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Contact's membership information");
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/demographics")
    @JsonView(Views.DemographicDetails.class)
    public Contact getDemographicDetails(@PathVariable("id") String id) {
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/demographics")
    public Response updateDemographicDetails(@PathVariable("id") String id, @RequestBody Contact details) {
        Contact contact = contactService.findById(id);

        if (null == contact) {
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.updateDemographicDetails(contact, details);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Contact with demographic details");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/committees", consumes= {"application/json"})
    public Response addContactToCommittee(@PathVariable("id") String id, @RequestBody IdDto idDto) {

        String idStringed = idDto.getId();

        Committee committee = committeeService.findById(idStringed);
        Contact contact = contactService.findById(id);

        if (committee == null) {
            logger.debug("No committee");
            return new Response(null,Response.FAILURE, "Committee with ID " + idStringed + " does not exist");

        } else if (contact == null) {
            logger.debug("No contact");
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.addContactToCommittee(contact, committee);
            return Response.successGeneric();
        } catch (Exception e) {
            logger.debug("Bad service call");
            return new Response(null, Response.FAILURE, "Unable to add contact with ID " + contact.getId() +
                    " to committee with ID " + committee.getId());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/committees/{comId}", consumes= {"application/json"})
    public Response removeContactFromCommittee(@PathVariable("id") String id, @PathVariable("comId") String comId) {

        Committee committee = committeeService.findById(comId);
        Contact contact = contactService.findById(id);

        if (committee == null) {
            logger.debug("No committee");
            return new Response(null,Response.FAILURE, "Committee with ID " + comId + " does not exist");

        } else if (contact == null) {
            logger.debug("No contact");
            return Response.failNonexistentContact(id);
        }

        try {
            contactService.removeContactFromCommittee(contact, committee);
            return Response.successGeneric();
        } catch (Exception e) {
            logger.debug("Bad service call");
            return new Response(null, Response.FAILURE, "Unable to remove contact with ID " + contact.getId() +
                    " from committee with ID " + committee.getId());
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/committees")
    @JsonView(Views.ContactCommitteeDetails.class)
    public Set<Committee> getAllCommitteesForContact(@PathVariable("id") String id) {
        return contactService.findById(id).getCommittees();
    }
}

