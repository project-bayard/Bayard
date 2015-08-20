package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.*;
import edu.usm.domain.exception.InvalidApiRequestException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.IdDto;
import edu.usm.dto.Response;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.SortedSet;

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
        String id = contactService.create(contact);
        return new Response(id, Response.SUCCESS);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.ContactDetails.class)
    public Contact getContactById(@PathVariable("id") String id) {
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes={"application/json"})
    public Response updateContactById(@PathVariable("id") String id, @RequestBody Contact details) throws NullDomainReference {
        contactService.updateBasicDetails(contactService.findById(id), details);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/events")
    @JsonView(Views.EventList.class)
    public Set<Event> getAttendedEvents(@PathVariable("id") String id) throws NullDomainReference{
        Contact contact = contactService.findById(id);
        if (null == contact) {
            throw new NullDomainReference.NullContact(id);
        }
        return contact.getAttendedEvents();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/events")
    public Response attendEvent(@PathVariable("id") String id, @RequestBody IdDto eventIdDto) throws NullDomainReference{
        Contact contact = contactService.findById(id);
        Event event = eventService.findById(eventIdDto.getId());

        try {
            contactService.attendEvent(contact, event);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullEvent e) {
            throw new NullDomainReference.NullEvent(eventIdDto.getId(), e);
        }

    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/events/{entityId}")
    public Response removeFromEvent(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws NullDomainReference {

        Contact contact =  contactService.findById(id);
        Event event = eventService.findById(entityId);

        try {
            contactService.unattendEvent(contact, event);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullEvent e) {
            throw new NullDomainReference.NullEvent(entityId, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/encounters")
    @JsonView(Views.ContactEncounterDetails.class)
    public SortedSet<Encounter> getAllEncountersForContact(@PathVariable("id") String id) throws NullDomainReference {
        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }
        return c.getEncounters();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/encounters/{entityId}")
    public Response deleteEncounter(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws NullDomainReference {

        Encounter encounter = encounterService.findById(entityId);

        try {
            encounterService.deleteEncounter(encounter);
            return Response.successGeneric();
        } catch (NullDomainReference.NullEncounter e) {
            throw new NullDomainReference.NullEncounter(entityId, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters/{encounterId}")
    public Response updateEncounter(@PathVariable("id") String id, @PathVariable("encounterId") String encounterId, @RequestBody EncounterDto encounterDto) throws NullDomainReference, InvalidApiRequestException{
        Contact contact = contactService.findById(id);
        Encounter encounter = encounterService.findById(encounterId);

        if (!contact.getId().equals(encounter.getContact().getId())) {
            throw new InvalidApiRequestException("The encounter with id "+encounterId+" does not belong to contact with id "+id);
        }

        try {
            encounterService.updateEncounter(encounter, encounterDto);
            return Response.successGeneric();
        } catch (NullDomainReference.NullEncounter e) {
            throw new NullDomainReference.NullEncounter(encounterId, e);
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(encounterDto.getInitiatorId(), e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters")
    public Response createEncounter(@PathVariable("id") String id, @RequestBody EncounterDto encounterDto) throws NullDomainReference{
        Contact contact = contactService.findById(id);
        Contact initiator = contactService.findById(encounterDto.getInitiatorId());

        try {
            contactService.addEncounter(contact, initiator, encounterDto);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id+" or "+encounterDto.getInitiatorId());
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
    public Set<Organization> getAllOrganizationsForContact(@PathVariable("id") String id) throws NullDomainReference{

        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }

        return c.getOrganizations();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/organizations", consumes= {"application/json"})
    public Response addContactToOrganization(@PathVariable("id") String id, @RequestBody IdDto idDto) throws NullDomainReference {

        Organization organization = organizationService.findById(idDto.getId());
        Contact contact = contactService.findById(id);

        try {
            contactService.addContactToOrganization(contact,organization);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullOrganization e) {
            throw new NullDomainReference.NullOrganization(idDto.getId(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/organizations/{entityId}")
    public Response removeContactFromOrganization(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws NullDomainReference{

        Organization organization = organizationService.findById(entityId);
        Contact contact = contactService.findById(id);

        try {
            contactService.removeContactFromOrganization(contact,organization);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullOrganization e) {
            throw new NullDomainReference.NullOrganization(entityId, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/memberinfo")
    @JsonView(Views.MemberInfo.class)
    public MemberInfo getMemberInfo(@PathVariable("id") String id) throws NullDomainReference{

        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }

        return c.getMemberInfo();

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/memberinfo")
    public Response updateMemberInfo(@PathVariable("id") String id, @RequestBody MemberInfo memberInfo) throws NullDomainReference{
        Contact contact = contactService.findById(id);
        try {
            contactService.updateMemberInfo(contact, memberInfo);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
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
    public Response updateDemographicDetails(@PathVariable("id") String id, @RequestBody Contact details) throws NullDomainReference{
        Contact contact = contactService.findById(id);

        try {
            contactService.updateDemographicDetails(contact, details);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/committees", consumes= {"application/json"})
    public Response addContactToCommittee(@PathVariable("id") String id, @RequestBody IdDto idDto) throws NullDomainReference{

        Committee committee = committeeService.findById(idDto.getId());
        Contact contact = contactService.findById(id);

        try {
            contactService.addContactToCommittee(contact, committee);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullCommittee e) {
            throw new NullDomainReference.NullCommittee(idDto.getId(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/committees/{entityId}")
    public Response removeContactFromCommittee(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws NullDomainReference {

        Committee committee = committeeService.findById(entityId);
        Contact contact = contactService.findById(id);

        try {
            contactService.removeContactFromCommittee(contact, committee);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullCommittee e) {
            throw new NullDomainReference.NullCommittee(id, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/committees")
    @JsonView(Views.ContactCommitteeDetails.class)
    public Set<Committee> getAllCommitteesForContact(@PathVariable("id") String id) throws NullDomainReference{
        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }
        return c.getCommittees();
    }
}

