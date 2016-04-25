package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.InvalidApiRequestException;
import edu.usm.domain.exception.NotFoundException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.*;
import edu.usm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.SortedSet;

/**
 * REST Controller for {@link Contact}
 */

@RestController
@RequestMapping(value = "/contacts" )
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * Returns a set of all existing contacts.
     * @return {@link Set} of {@link Contact}
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getContacts() {
        return contactService.findAll();
    }

    /**
     * Creates a new Contact
     * @param contact
     * @return The UUID of the new contact.
     * @throws ConstraintViolation
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
    public Response createContact(@RequestBody Contact contact) throws ConstraintViolation {
        String id = contactService.create(contact);
        return new Response(id, Response.SUCCESS);
    }

    /**
     * Deletes a Contact with the given UUID
     * @param id The id of the Contact to delete
     * @return
     * @throws ConstraintViolation
     * @throws NullDomainReference
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
    public Response deleteContact(@PathVariable("id") String id) throws ConstraintViolation, NullDomainReference {
        contactService.delete(id);
        return new Response(id, Response.SUCCESS);
    }

    /**
     * Find a Contact by it's UUID
     * @param id
     * @return {@link Contact}
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.ContactDetails.class)
    public Contact getContactById(@PathVariable("id") String id) throws NullDomainReference {
        return contactService.findById(id);
    }

    /**
     * Updates the basic details of a contact. Basic details are the fields that are not collections or references to
     * other domain objects.
     * @param id
     * @param details
     * @return
     * @throws ConstraintViolation
     * @throws NullDomainReference
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes={"application/json"})
    public Response updateContactById(@PathVariable("id") String id, @RequestBody Contact details) throws  ConstraintViolation,  NullDomainReference {
        contactService.updateBasicDetails(id, details);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/events")
    @JsonView(Views.EventList.class)
    public Set<Event> getAttendedEvents(@PathVariable("id") String id) throws NullDomainReference {
        return contactService.getAllContactEvents(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/events")
    public Response attendEvent(@PathVariable("id") String id, @RequestBody IdDto eventIdDto) throws ConstraintViolation,  NullDomainReference{
        contactService.attendEvent(id, eventIdDto.getId());
        return Response.successGeneric();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/events/{eventId}")
    public Response removeFromEvent(@PathVariable("id") String id, @PathVariable("eventId") String eventId) throws  ConstraintViolation, NullDomainReference {
        contactService.unattendEvent(id, eventId);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/encounters")
    @JsonView(Views.ContactEncounterDetails.class)
    public SortedSet<Encounter> getAllEncountersForContact(@PathVariable("id") String id) throws NullDomainReference {
        return contactService.getAllContactEncounters(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/encounters/{encounterId}")
    public Response deleteEncounter(@PathVariable("id") String id, @PathVariable("encounterId") String encounterId) throws  ConstraintViolation, NullDomainReference {
        contactService.removeEncounter(id, encounterId);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters/{encounterId}")
    public Response updateEncounter(@PathVariable("id") String id, @PathVariable("encounterId") String encounterId, @RequestBody EncounterDto encounterDto)
            throws ConstraintViolation,  NullDomainReference, InvalidApiRequestException{
        contactService.updateEncounter(id, encounterId, encounterDto);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters")
    public Response createEncounter(@PathVariable("id") String id, @RequestBody EncounterDto encounterDto) throws  ConstraintViolation, NullDomainReference{
        contactService.addEncounter(id, encounterDto);
        return Response.successGeneric();
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
        return contactService.getAllContactOrganizations(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/organizations", consumes= {"application/json"})
    public Response addContactToOrganization(@PathVariable("id") String id, @RequestBody IdDto idDto) throws ConstraintViolation,  NullDomainReference {
        contactService.addContactToOrganization(id,idDto.getId());
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/organizations/{organizationId}")
    public Response removeContactFromOrganization(@PathVariable("id") String id, @PathVariable("organizationId") String organizationId) throws ConstraintViolation,  NullDomainReference{
        contactService.removeContactFromOrganization(id, organizationId);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/memberinfo")
    @JsonView(Views.MemberInfo.class)
    public MemberInfo getMemberInfo(@PathVariable("id") String id) throws NullDomainReference {
        return contactService.getContactMemberInfo(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/memberinfo")
    public Response updateMemberInfo(@PathVariable("id") String id, @RequestBody MemberInfo memberInfo) throws  ConstraintViolation, NullDomainReference{
        contactService.updateMemberInfo(id, memberInfo);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/demographics")
    @JsonView(Views.DemographicDetails.class)
    public Contact getDemographicDetails(@PathVariable("id") String id) throws NullDomainReference {
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/demographics")
    public Response updateDemographicDetails(@PathVariable("id") String id, @RequestBody Contact details) throws  ConstraintViolation, NullDomainReference{
        contactService.updateDemographicDetails(id, details);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/committees", consumes= {"application/json"})
    public Response addContactToCommittee(@PathVariable("id") String id, @RequestBody IdDto committeeId) throws  ConstraintViolation, NullDomainReference{
        contactService.addContactToCommittee(id, committeeId.getId());
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/committees/{committeeId}")
    public Response removeContactFromCommittee(@PathVariable("id") String id, @PathVariable("committeeId") String committeeId) throws  ConstraintViolation, NullDomainReference {
        contactService.removeContactFromCommittee(id, committeeId);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/committees")
    @JsonView(Views.ContactCommitteeDetails.class)
    public Set<Committee> getAllCommitteesForContact(@PathVariable("id") String id) throws NullDomainReference{
       return contactService.getAllContactCommittees(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/groups")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GroupPanel.class)
    public Set<Group> getContactGroups(@PathVariable("id") String id) throws NullDomainReference {
        return contactService.getAllContactGroups(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/groups", consumes= {"application/json"})
    public Response addContactToGroup(@PathVariable("id") String id, @RequestBody IdDto groupIdDto) throws NullDomainReference{
        contactService.addToGroup(id, groupIdDto.getId());
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/groups/{groupId}")
    public Response removeContactFromGroup(@PathVariable("id") String id, @PathVariable("groupId") String groupId) throws NullDomainReference, NullDomainReference {
        contactService.removeFromGroup(id, groupId);
        return Response.successGeneric();
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.ContactDetails.class)
    public Contact findByFirstLastEmailPhone(@RequestBody SignInDto dto) throws NotFoundException {
        return contactService.findByFirstEmailPhone(dto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/donations", method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
    public Response addDonation(@PathVariable("id")String id, @RequestBody DonationDto donationDto) throws NullDomainReference, ConstraintViolation {
        contactService.addDonation(id, donationDto);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/donations/{donationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response removeDonation(@PathVariable("id")String id, @PathVariable("donationId")String donationId) throws NullDomainReference, ConstraintViolation {
        contactService.removeDonation(id, donationId);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/donations", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.DonationDetails.class)
    public Set<Donation> getDonationsForContact(@PathVariable("id")String id) throws NullDomainReference {
        return contactService.getAllContactDonations(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/sustainer/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.SustainerPeriodDetails.class)
    public SustainerPeriod getSustainerPeriod(@PathVariable("id")String id, @PathVariable("entityId")String sustainerPeriodId) throws NullDomainReference {
        return contactService.findSustainerPeriodById(sustainerPeriodId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/sustainer", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.SustainerPeriodDetails.class)
    public Set<SustainerPeriod> getSustainerPeriodsForContact(@PathVariable("id")String id) throws NullDomainReference {
        return contactService.getAllContactSustainerPeriods(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/sustainer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createSustainerPeriod(@PathVariable("id")String id, @RequestBody SustainerPeriodDto dto) throws NullDomainReference, ConstraintViolation {
        contactService.createSustainerPeriod(id, dto);
        return Response.successGeneric();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/sustainer/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateSustainerPeriod (@PathVariable("id")String id, @PathVariable("entityId")String sustainerPeriodId, @RequestBody SustainerPeriodDto dto)
            throws NullDomainReference, ConstraintViolation {
        contactService.updateSustainerPeriod(id, sustainerPeriodId, dto);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/sustainer/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteSustainerPeriod(@PathVariable("id")String id, @PathVariable("entityId")String sustainerPeriodId) throws NullDomainReference, ConstraintViolation {
        contactService.deleteSustainerPeriod(id, sustainerPeriodId);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/currentSustainers", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.ContactList.class)
    public Set<Contact> getAllCurrentSustainers() {
        return contactService.findAllCurrentSustainers();
    }

}

