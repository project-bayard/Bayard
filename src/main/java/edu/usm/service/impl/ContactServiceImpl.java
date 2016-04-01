package edu.usm.service.impl;

import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.SignInDto;
import edu.usm.dto.SustainerPeriodDto;
import edu.usm.repository.ContactDao;
import edu.usm.repository.SustainerPeriodDao;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by scottkimball on 3/12/15.
 */
@Service
public class ContactServiceImpl extends BasicService implements ContactService {

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private SustainerPeriodDao sustainerPeriodDao;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CommitteeService committeeService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EncounterService encounterService;
    @Autowired
    private GroupService groupService;

    private Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public void attendEvent(Contact contact, Event event) throws NullDomainReference.NullContact, NullDomainReference.NullEvent {

        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        if (null == event) {
            throw new NullDomainReference.NullEvent();
        }

        Set<Event> attendedEvents = contact.getAttendedEvents();
        Set<Contact> attendees = event.getAttendees();

        if (null == attendedEvents) {
            attendedEvents = new HashSet<>();
            contact.setAttendedEvents(attendedEvents);
        }

        if (null == attendees) {
            attendees = new HashSet<>();
            event.setAttendees(attendees);
        }

        attendees.add(contact);
        attendedEvents.add(event);

        update(contact);
    }

    @Override
    public Contact findById(String id)  {
        if (null == id) {
            return null;
        }
        return contactDao.findOne(id);
    }

    @Override
    public Set<Contact> findAll() {
        logger.debug("Finding all Contacts");
        return (Set<Contact>) contactDao.findAll();
    }

    @Override
    @Transactional
    public void delete(Contact contact) throws ConstraintViolation, NullDomainReference {

        updateLastModified(contact);

        /*Remove from organizations */
        if (contact.getOrganizations() != null) {
            for(Organization organization : contact.getOrganizations()) {
                organization.getMembers().remove(contact);
                removeContactFromOrganization(contact.getId(),organization.getId());
            }
        }

        /*Remove from committees*/
        if (contact.getCommittees() != null) {
            for(Committee committee : contact.getCommittees()) {
                committee.getMembers().remove(contact);
                committeeService.update(committee);
            }
        }

         /*Remove from attended events*/
        if (contact.getAttendedEvents() != null) {
            for(Event event : contact.getAttendedEvents()) {
                event.getAttendees().remove(contact);
                eventService.update(event);
            }
        }

        if (contact.getGroups() != null) {
            for (Group group: contact.getGroups()) {
                group.getTopLevelMembers().remove(contact);
                groupService.update(group);
            }
        }

        contact.getEncounters().clear();

        if (contact.getEncountersInitiated() != null) {
            for (Encounter encounter : contact.getEncountersInitiated()) {
                encounter.setInitiator(null);
                encounterService.updateEncounter(encounter,null, null);
            }
            contact.getEncountersInitiated().clear();
        }

        contactDao.delete(contact);
    }

    private void update(Contact contact) {
        updateLastModified(contact);
        contactDao.save(contact);
    }


    private void validateOnUpdate(Contact contact) throws ConstraintViolation {
        emptyStringToNull(contact);

        String first = contact.getFirstName();
        String email = contact.getEmail();
        String phone = contact.getPhoneNumber1();
        Contact result;

        if (first == null) {
            throw new ConstraintViolation(ConstraintMessage.CONTACT_NO_FIRST_NAME);

        } else if (email != null) {
            result = contactDao.findOneByFirstNameAndEmail(first, email);
            if (result != null && !contact.getId().equals(result.getId())) {
                throw new ConstraintViolation(ConstraintMessage.CONTACT_DUPLICATE_NAME_EMAIL);
            }

        } else if (phone != null) {
            result = contactDao.findOneByFirstNameAndPhoneNumber1(first, phone);
            if (result != null && !contact.getId().equals(result.getId())) {
                throw new ConstraintViolation(ConstraintMessage.CONTACT_DUPLICATE_NAME_PHONE_NUMBER);
            }

        } else {
            throw new ConstraintViolation(ConstraintMessage.CONTACT_NO_EMAIL_OR_PHONE_NUMBER);
        }

    }


    @Override
    public String create(Contact contact) throws ConstraintViolation {
        validateOnCreate(contact);
        contactDao.save(contact);
        return contact.getId();
    }

    private void validateOnCreate(Contact contact) throws ConstraintViolation {
        emptyStringToNull(contact);

        String first = contact.getFirstName();
        String email = contact.getEmail();
        String phone = contact.getPhoneNumber1();
        Contact result;

        if (first == null) {
            throw new ConstraintViolation(ConstraintMessage.CONTACT_NO_FIRST_NAME);

        } else if (email != null) {
            result = contactDao.findOneByFirstNameAndEmail(first, email);
            if (result != null) {
                throw new ConstraintViolation(ConstraintMessage.CONTACT_DUPLICATE_NAME_EMAIL);
            }

        } else if (phone != null) {
            result = contactDao.findOneByFirstNameAndPhoneNumber1(first, phone);
            if (result != null) {
                throw new ConstraintViolation(ConstraintMessage.CONTACT_DUPLICATE_NAME_PHONE_NUMBER);
            }

        } else {
            throw new ConstraintViolation(ConstraintMessage.CONTACT_NO_EMAIL_OR_PHONE_NUMBER);
        }
    }

    public Set<Contact> findByFirstName(String firstName) {
        return contactDao.findByFirstName(firstName);
    }

    /*
    Satisfies Java 8 method stream expectations of exception type
     */
    private void uncheckedDelete(Contact contact) {
        try {
            delete(contact);
        } catch (NullDomainReference e) {
            throw new RuntimeException(e);
        } catch (ConstraintViolation e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteAll() {

        logger.debug("Deleting all contacts.");
        Set<Contact> contacts = findAll();
        contacts.stream().forEach(this::uncheckedDelete);
    }

    @Override
    public Set<Contact> findAllInitiators() {
        logger.debug("Getting all Initiators");
        return contactDao.findAllInitiators();
    }

    @Override
    public void removeFromGroup(Contact contact, Group group) {
        contact.getGroups().remove(group);
        group.getTopLevelMembers().remove(contact);
        update(contact);
    }

    @Override
    public void addToGroup(Contact contact, Group group) {
        group.getTopLevelMembers().add(contact);
        if (null == contact.getGroups()) {
            contact.setGroups(new HashSet<>());
        }
        contact.getGroups().add(group);
        update(contact);
    }

    @Override
    public void addDonation(Contact contact, Donation donation) throws NullDomainReference.NullContact {
        if (null == contact.getDonorInfo()) {
            contact.setDonorInfo(new DonorInfo());
        }
        contact.getDonorInfo().addDonation(donation);
        updateLastModified(donation);
        update(contact);
    }

    @Override
    public void removeDonation(Contact contact, Donation donation) throws NullDomainReference.NullContact {
        if (null != contact.getDonorInfo() && null != contact.getDonorInfo().getDonations()) {
            contact.getDonorInfo().getDonations().remove(donation);
            updateLastModified(contact.getDonorInfo());
            updateLastModified(donation);
            update(contact);
        }
    }

    @Override
    public SustainerPeriod findSustainerPeriodById(String id) {
        return sustainerPeriodDao.findOne(id);
    }

    @Override
    public void createSustainerPeriod(Contact contact, SustainerPeriod sustainerPeriod) throws ConstraintViolation {
        if (null == contact.getDonorInfo()) {
            contact.setDonorInfo(new DonorInfo());
        }
        contact.getDonorInfo().addSustainerPeriod(sustainerPeriod);
        sustainerPeriod.setDonorInfo(contact.getDonorInfo());
        updateLastModified(contact.getDonorInfo());
        updateLastModified(sustainerPeriod);
        update(contact);
    }

    @Override
    public void createSustainerPeriod(Contact contact, SustainerPeriodDto dto) throws ConstraintViolation {
        SustainerPeriod sustainerPeriod = new SustainerPeriod();
        sustainerPeriod = DtoTransformer.fromDto(dto, sustainerPeriod);
        createSustainerPeriod(contact, sustainerPeriod);
    }

    @Override
    public void updateSustainerPeriod(Contact contact, SustainerPeriod existing, SustainerPeriodDto newDetails) throws ConstraintViolation {
        if (null != contact.getDonorInfo() && null != contact.getDonorInfo().getSustainerPeriods()) {
            contact.getDonorInfo().getSustainerPeriods().remove(existing);
            existing = DtoTransformer.fromDto(newDetails, existing);
            if (null == existing.getPeriodStartDate()) {
                throw new ConstraintViolation(ConstraintMessage.SUSTAINER_PERIOD_NO_START_DATE);
            }
            contact.getDonorInfo().addSustainerPeriod(existing);
            updateLastModified(contact.getDonorInfo());
            updateLastModified(existing);
            update(contact);
        }
    }

    @Override
    public void deleteSustainerPeriod(Contact contact, SustainerPeriod sustainerPeriod) {
        if (null != contact.getDonorInfo() && null != contact.getDonorInfo().getSustainerPeriods()) {
            contact.getDonorInfo().getSustainerPeriods().remove(sustainerPeriod);
            updateLastModified(sustainerPeriod);
            updateLastModified(contact.getDonorInfo());
            update(contact);
        }
    }

    @Override
    @Transactional
    public void addContactToOrganization(String contactId, String organizationId) throws NullDomainReference.NullOrganization, NullDomainReference.NullContact{

        Contact contact = contactDao.findOne(contactId);
        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        Organization organization = organizationService.findById(organizationId);
        if (null == organization) {
            throw new NullDomainReference.NullOrganization();
        }

        Set<Organization> organizations = contact.getOrganizations();
        Set<Contact> members = organization.getMembers();

        if (organizations == null) {
            organizations = new HashSet<>();
            contact.setOrganizations(organizations);
        }

        if (members == null) {
            members = new HashSet<>();
            organization.setMembers(members);
        }

        members.add(contact);
        organizations.add(organization);

        update(contact);
    }


    @Override
    @Transactional
    public void removeContactFromOrganization(String contactId, String organizationId) throws NullDomainReference.NullContact, NullDomainReference.NullOrganization{
        Contact contact = contactDao.findOne(contactId);
        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        Organization organization = organizationService.findById(organizationId);
        if (null == organization) {
            throw new NullDomainReference.NullOrganization();
        }

        Set<Organization> organizations = contact.getOrganizations();
        Set<Contact> members = organization.getMembers();

        if (organizations == null || members == null) {
            return;
        }

        members.remove(contact);
        organizations.remove(organization);
        update(contact);
    }

    @Override
    @Transactional
    public Set<Organization> getAllContactOrganizations(String id) throws NullDomainReference.NullContact {
        Contact contact = contactDao.findOne(id);
        if (contact == null) {
            throw new NullDomainReference.NullContact(id);
        }

        Set<Organization> organizations = contact.getOrganizations();
        if (organizations == null) {
            return new HashSet<>();
        } else {
            organizations.size();
            return organizations;
        }
    }

    @Override
    public void addContactToCommittee(Contact contact, Committee committee) throws NullDomainReference.NullContact, NullDomainReference.NullCommittee{

        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        if (null == committee) {
            throw new NullDomainReference.NullCommittee();
        }

        Set<Committee> committees = contact.getCommittees();
        Set<Contact> members = committee.getMembers();

        if (committees == null) {
            committees = new HashSet<>();
            contact.setCommittees(committees);
        }

        if (members == null) {
            members = new HashSet<>();
            committee.setMembers(members);
        }

        members.add(contact);
        committees.add(committee);


        update(contact);
    }

    @Override
    public void removeContactFromCommittee(Contact contact, Committee committee) throws NullDomainReference.NullContact, NullDomainReference.NullCommittee{

        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        if (null == committee) {
            throw new NullDomainReference.NullCommittee();
        }

        Set<Committee> committees = contact.getCommittees();
        Set<Contact> members = committee.getMembers();

        if (committees == null || members == null) {
            return;
        }

        members.remove(contact);
        committees.remove(committee);

        update(contact);
    }

    @Override
    public void updateBasicDetails(Contact contact, Contact details) throws ConstraintViolation, NullDomainReference.NullContact {

        if (null == contact || null == details) {
            throw new NullDomainReference.NullContact();
        }

        contact.setFirstName(details.getFirstName());
        contact.setMiddleName(details.getMiddleName());
        contact.setLastName(details.getLastName());
        contact.setStreetAddress(details.getStreetAddress());
        contact.setAptNumber(details.getAptNumber());
        contact.setCity(details.getCity());
        contact.setState(details.getState());
        contact.setZipCode(details.getZipCode());
        contact.setPhoneNumber1(details.getPhoneNumber1());
        contact.setPhoneNumber2(details.getPhoneNumber2());
        contact.setEmail(details.getEmail());
        contact.setLanguage(details.getLanguage());
        contact.setOccupation(details.getOccupation());
        contact.setInterests(details.getInterests());
        contact.setInitiator(details.isInitiator());
        contact.setNeedsFollowUp(details.needsFollowUp());


        validateOnUpdate(contact);
        update(contact);

    }

    @Override
    public void unattendEvent(Contact contact, Event event) throws  ConstraintViolation, NullDomainReference.NullContact, NullDomainReference.NullEvent {

        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        if (null == event) {
            throw new NullDomainReference.NullEvent();
        }

        if (contact.getAttendedEvents() != null) {
            contact.getAttendedEvents().remove(event);
            event.getAttendees().remove(contact);

            update(contact);
            eventService.update(event);
        }
    }

    @Override
    public void updateDemographicDetails(Contact contact, Contact details) throws  NullDomainReference.NullContact {

        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        contact.setRace(details.getRace());
        contact.setEthnicity(details.getEthnicity());
        contact.setDateOfBirth(details.getDateOfBirth());
        contact.setGender(details.getGender());
        contact.setDisabled(details.isDisabled());
        contact.setIncomeBracket(details.getIncomeBracket());
        contact.setSexualOrientation(details.getSexualOrientation());


        update(contact);
    }

    @Override
    public void addEncounter(Contact contact, Contact initiator, EncounterType encounterType, EncounterDto dto)
            throws ConstraintViolation, NullDomainReference.NullContact, NullDomainReference.NullEncounterType {

        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        if (null == initiator) {
            throw new NullDomainReference.NullContact();
        } else if (!initiator.isInitiator()) {
            throw new ConstraintViolation(ConstraintMessage.ENCOUNTER_CONTACT_NOT_INITIATOR);
        }

        if (null == encounterType) {
            throw new NullDomainReference.NullEncounterType();
        }

        Encounter encounter = new Encounter();
        encounter.setEncounterDate(dto.getEncounterDate());
        encounter.setContact(contact);
        encounter.setInitiator(initiator);
        encounter.setNotes(dto.getNotes());
        encounter.setType(encounterType.getName());
        encounter.setAssessment(dto.getAssessment());
        encounter.setRequiresFollowUp(dto.requiresFollowUp());

        if (null == encounter.getEncounterDate()) {
            throw new ConstraintViolation(ConstraintMessage.ENCOUNTER_REQUIRED_DATE);
        }

        //update assessment and follow up indicator if this is the most recent encounter
        contact.getEncounters().add(encounter);
        contact.setNeedsFollowUp(contact.getEncounters().first().requiresFollowUp());
        contact.setAssessment(getUpdatedAssessment(contact));

        update(contact);

        if (null == initiator.getEncountersInitiated()) {
            initiator.setEncountersInitiated(new TreeSet<>());
        }
        initiator.getEncountersInitiated().add(encounter);


        update(initiator);
    }

    @Override
    public int getUpdatedAssessment(Contact contact) {
        /*Sets assessment to most recent encounter assessment */
        if (null == contact.getEncounters() || contact.getEncounters().isEmpty()) {
            return contact.getAssessment();
        }

        int currentAssessment = contact.getEncounters().first().getAssessment() == Encounter.DEFAULT_ASSESSMENT ? contact.getAssessment() :
                contact.getEncounters().first().getAssessment();
        return currentAssessment;

    }

    @Override
    public void updateNeedsFollowUp(Contact contact, boolean followUp)   {
        contact.setNeedsFollowUp(followUp);

        update(contact);
    }

    @Override
    public void removeEncounter(Contact contact, Encounter encounter)   {
        contact.getEncounters().remove(encounter);
        contact.setAssessment(getUpdatedAssessment(contact));
        encounter.setContact(null);

        Contact initiator = encounter.getInitiator();
        if (null != initiator) {
            initiator.getEncountersInitiated().remove(encounter);
            encounter.setInitiator(null);
            update(initiator);
        }

        update(contact);
    }

    @Override
    public void removeInitiator(Contact initiator, Encounter encounter)  {
        initiator.getEncountersInitiated().remove(encounter);
        encounter.setInitiator(null);
        update(initiator);
    }

    @Override
    public void updateMemberInfo(Contact contact, MemberInfo memberInfo) throws NullDomainReference.NullContact{
        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }
        contact.setMemberInfo(memberInfo);

        update(contact);
    }

    @Override
    public void updateAssessment(Contact contact, int assessment) {
        contact.setAssessment(assessment);

        update(contact);
    }

    @Override
    public Contact findByFirstEmailPhone(SignInDto signInDto) {

        if (signInDto.getFirstName() == null || (signInDto.getEmail() == null && signInDto.getPhoneNumber() == null)) {
            return null;

        }else if (signInDto.getEmail() != null) {
            return contactDao.findOneByFirstNameAndEmail(signInDto.getFirstName(), signInDto.getEmail());
        }

        Contact contact = contactDao.findOneByFirstNameAndPhoneNumber1(signInDto.getFirstName(), signInDto.getPhoneNumber());

        if (contact != null) {
            return  contact;
        } else {
            return contactDao.findOneByFirstNameAndPhoneNumber2(signInDto.getFirstName(), signInDto.getPhoneNumber());
        }
    }
}
