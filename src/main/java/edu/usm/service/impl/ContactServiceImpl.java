package edu.usm.service.impl;

import edu.usm.domain.*;
import edu.usm.domain.exception.*;
import edu.usm.dto.*;
import edu.usm.repository.ContactDao;
import edu.usm.repository.DonorInfoDao;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by scottkimball on 3/12/15.
 */
@Service
public class ContactServiceImpl extends DonationAssigningService implements ContactService {

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private DonorInfoDao donorInfoDao;
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
    @Autowired
    private DonationService donationService;
    @Autowired
    private SustainerPeriodService sustainerPeriodService;
    @Autowired
    private EncounterTypeService encounterTypeService;

    private Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    @Transactional
    public void attendEvent(String contactId, String eventId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Event event = eventService.findById(eventId);

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
    @Transactional
    public Set<Event> getAllContactEvents(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Set<Event> events = contact.getAttendedEvents();
        if (events == null) {
            events = new HashSet<>();
        }
        events.size();
        return events;

    }

    @Override
    public Contact findById(String id) throws NullDomainReference {
        return findContact(id);
    }

    @Override
    public Set<Contact> findAll() {
        logger.debug("Finding all Contacts");
        return (Set<Contact>) contactDao.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) throws ConstraintViolation, NullDomainReference {
        Contact contact = findContact(id);
        /*Remove from organizations */
        if (contact.getOrganizations() != null) {
            Set<Organization> organizations = new HashSet<>(contact.getOrganizations());
            for(Organization organization : organizations) {
                organization.getMembers().remove(contact);
                removeContactFromOrganization(contact.getId(),organization.getId());
            }
        }

        /*Remove from committees*/
        if (contact.getCommittees() != null) {
            Set<Committee> committees = new HashSet<>(contact.getCommittees());
            for(Committee committee : committees) {
                committee.getMembers().remove(contact);
                committeeService.update(committee.getId(),committee);
            }
        }

         /*Remove from attended events*/
        if (contact.getAttendedEvents() != null) {
            Set<Event> events = new HashSet<>(contact.getAttendedEvents());
            for(Event event : events) {
                event.getAttendees().remove(contact);
                eventService.removeContactFromEvent(contact.getId(),event.getId());
            }
        }

        if (contact.getGroups() != null) {
            Set<Group> groups = new HashSet<>(contact.getGroups());
            for (Group group: groups) {
                group.getTopLevelMembers().remove(contact);
                groupService.update(group);
            }
        }

        if (contact.getEncounters() != null) {
            for (Encounter en: new HashSet<>(contact.getEncounters())) {
                removeEncounter(contact.getId(), en.getId());
            }
            contact.getEncounters().clear();
        }


        if (contact.getEncountersInitiated() != null) {
            for (Encounter en: new HashSet<>(contact.getEncountersInitiated())) {
                removeInitiator(contact.getId(), en.getId());
            }
            contact.getEncountersInitiated().clear();
        }

        updateLastModified(contact);
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
    @Transactional
    public void removeFromGroup(String contactId, String groupId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Group group = groupService.findById(groupId);
        if (group == null) {
            throw new NullDomainReference.NullGroup(groupId);
        }

        contact.getGroups().remove(group);
        group.getTopLevelMembers().remove(contact);
        update(contact);
    }

    @Override
    @Transactional
    public void addToGroup(String contactId, String groupId) throws NullDomainReference{
        Group group = groupService.findById(groupId);
        Contact contact = findContact(contactId);
        group.getTopLevelMembers().add(contact);
        if (null == contact.getGroups()) {
            contact.setGroups(new HashSet<>());
        }
        contact.getGroups().add(group);
        update(contact);
    }

    @Override
    @Transactional
    public Set<Group> getAllContactGroups(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Set<Group> groups = contact.getGroups();
        if (groups == null) {
            groups = new HashSet<>();
        }
        groups.size();
        return groups;
    }

    @Override
    @Transactional
    public void addDonation(String contactId, DonationDto donationDto) throws NullDomainReference{
        Donation donation = DtoTransformer.fromDto(donationDto);
        Contact contact = findContact(contactId);
        if (null == contact.getDonorInfo()) {
            contact.setDonorInfo(new DonorInfo());
        }

        if (donationDto.getBudgetItemId() != null) {
            BudgetItem item = donationService.findBudgetItem(donationDto.getBudgetItemId());

            if (item == null) {
                throw new NullDomainReference.NullBudgetItem(donationDto.getBudgetItemId());
            }
            donation.setBudgetItem(item);
        }

        contact.getDonorInfo().addDonation(donation);
        updateLastModified(donation);
        update(contact);
    }

    @Override
    @Transactional
    public void removeDonation(String contactId, String donationId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Donation donation = donationService.findById(donationId);
        if (donation == null) {
            throw new NullDomainReference.NullDonation(donationId);
        }
        if (null != contact.getDonorInfo() && null != contact.getDonorInfo().getDonations()) {
            contact.getDonorInfo().getDonations().remove(donation);
            updateLastModified(contact.getDonorInfo());
            updateLastModified(donation);
            update(contact);
        }
    }

    @Override
    @Transactional
    public Set<Donation> getAllContactDonations(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);

        Set<Donation> donations = new HashSet<>();
        if (contact.getDonorInfo() != null && contact.getDonorInfo().getDonations() != null) {
            donations = contact.getDonorInfo().getDonations();
        }

        donations.size();
        return donations;
    }

    @Override
    @Transactional
    public DonorInfo getDonorInfo(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        if (contact.isDonor()) {
            DonorInfo donorInfo = contact.getDonorInfo();
            donorInfo.getLastModified();
            return donorInfo;
        } else {
            return null; //TODO What should happen if there is no donor info here?
        }

        }

    public Contact findContactWithDonation(Donation donation) {
        DonorInfo donorInfo = donorInfoDao.findByDonations_id(donation.getId());
        return (null == donorInfo) ? null : contactDao.findOneByDonorInfo(donorInfo);
    }

    @Override
    public SustainerPeriod findSustainerPeriodById(String id) {
        return sustainerPeriodService.findById(id);
    }


    @Override
    @Transactional
    public void createSustainerPeriod(String contactId, SustainerPeriodDto dto) throws ConstraintViolation, NullDomainReference {
        Contact contact = findContact(contactId);
        SustainerPeriod sustainerPeriod = new SustainerPeriod();
        sustainerPeriod = DtoTransformer.fromDto(dto, sustainerPeriod);

        if (null == contact.getDonorInfo()) {
            contact.setDonorInfo(new DonorInfo());
        }
        contact.getDonorInfo().addSustainerPeriod(sustainerPeriod);
        sustainerPeriod.setDonorInfo(contact.getDonorInfo());
        if (null == sustainerPeriod.getCancelDate()) {
            contact.getDonorInfo().setCurrentSustainer(true);
        }
        updateLastModified(contact.getDonorInfo());
        updateLastModified(sustainerPeriod);
        update(contact);
    }

    @Override
    @Transactional
    public void updateSustainerPeriod(String contactId, String sustainerPeriodId, SustainerPeriodDto newDetails) throws ConstraintViolation, NullDomainReference {
        Contact contact = findContact(contactId);
        SustainerPeriod existing = sustainerPeriodService.findById(sustainerPeriodId);
        if (null != contact.getDonorInfo() && null != contact.getDonorInfo().getSustainerPeriods()) {
            contact.getDonorInfo().getSustainerPeriods().remove(existing);
            existing = DtoTransformer.fromDto(newDetails, existing);
            if (null == existing.getPeriodStartDate()) {
                throw new ConstraintViolation(ConstraintMessage.SUSTAINER_PERIOD_NO_START_DATE);
            }

            contact.getDonorInfo().addSustainerPeriod(existing);
            refreshContactSustainerStatus(contact);

            updateLastModified(contact.getDonorInfo());
            updateLastModified(existing);
            update(contact);
        }
    }

    @Override
    @Transactional
    public void deleteSustainerPeriod(String contactId, String sustainerPeriodId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        SustainerPeriod sustainerPeriod = sustainerPeriodService.findById(sustainerPeriodId);
        if (null != contact.getDonorInfo() && null != contact.getDonorInfo().getSustainerPeriods()) {
            contact.getDonorInfo().getSustainerPeriods().remove(sustainerPeriod);
            refreshContactSustainerStatus(contact);
            updateLastModified(sustainerPeriod);
            updateLastModified(contact.getDonorInfo());
            update(contact);
        }
    }

    @Override
    @Transactional
    public Set<SustainerPeriod> getAllContactSustainerPeriods(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        DonorInfo donorInfo = contact.getDonorInfo();
        if (donorInfo == null) {
            return new TreeSet<>(); // returns an empty set since the contact wouldn't have any sustainer periods
        }
        Set<SustainerPeriod> sustainerPeriods = donorInfo.getSustainerPeriods();
        if (sustainerPeriods == null) {
            sustainerPeriods = new TreeSet<>();
        }
        sustainerPeriods.size();
        return sustainerPeriods;

    }

    @Override
    public Set<Contact> findAllCurrentSustainers() {
        return contactDao.findByDonorInfoCurrentSustainer(true);
    }


    @Override
    @Transactional
    public void addContactToOrganization(String contactId, String organizationId) throws NullDomainReference {
        Contact contact = findContact(contactId);
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
    public void removeContactFromOrganization(String contactId, String organizationId) throws NullDomainReference {
        Contact contact = findContact(contactId);
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
    public Set<Organization> getAllContactOrganizations(String id) throws NullDomainReference {
        Contact contact = findContact(id);
        Set<Organization> organizations = contact.getOrganizations();
        if (organizations == null) {
            return new HashSet<>();
        } else {
            organizations.size();
            return organizations;
        }
    }

    @Override
    @Transactional
    public Set<Committee> getAllContactCommittees(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Set<Committee> committees = contact.getCommittees();
        if (committees == null) {
            committees = new HashSet<>();
        }
        committees.size();
        return committees;
    }

    @Override
    @Transactional
    public void addContactToCommittee(String contactId, String committeeId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Committee committee = committeeService.findById(committeeId);

        if (committee == null) {
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
    @Transactional
    public void removeContactFromCommittee(String contactId, String committeeId) throws NullDomainReference {
        Contact contact = contactDao.findOne(contactId);
        if (null == contact) {
            throw new NullDomainReference.NullContact();
        }

        Committee committee = committeeService.findById(committeeId);
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
    public void updateBasicDetails(String contactId, Contact details) throws ConstraintViolation, NullDomainReference {
        Contact contact = findContact(contactId);
        contact.setFirstName(details.getFirstName());
        contact.setMiddleName(details.getMiddleName());
        contact.setLastName(details.getLastName());
        contact.setNickName(details.getNickName());
        contact.setStreetAddress(details.getStreetAddress());
        contact.setAptNumber(details.getAptNumber());
        contact.setCity(details.getCity());
        contact.setState(details.getState());
        contact.setZipCode(details.getZipCode());
        contact.setMailingStreetAddress(details.getMailingStreetAddress());
        contact.setMailingAptNumber(details.getMailingAptNumber());
        contact.setMailingCity(details.getMailingCity());
        contact.setMailingState(details.getMailingState());
        contact.setMailingZipCode(details.getMailingZipCode());
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
    @Transactional
    public void unattendEvent(String contactId, String eventId) throws  ConstraintViolation, NullDomainReference {
        Contact contact = findContact(contactId);
        Event event = eventService.findById(eventId);

        if (null == event) {
            throw new NullDomainReference.NullEvent();
        }

        if (contact.getAttendedEvents() != null) {
            contact.getAttendedEvents().remove(event);
            event.getAttendees().remove(contact);
            update(contact);
            eventService.removeContactFromEvent(contactId,eventId);
        }
    }

    @Override
    public void updateDemographicDetails(String contactId, Contact details) throws  NullDomainReference {
        Contact contact = findContact(contactId);
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
    @Transactional
    public void addEncounter(String contactId,  EncounterDto dto) throws ConstraintViolation, NullDomainReference {
        Contact contact = findContact(contactId);
        Contact initiator = findContact(dto.getInitiatorId());
        EncounterType encounterType = encounterTypeService.findById(dto.getType());

        if (!initiator.isInitiator()) {
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
        contact.setAssessment(getUpdatedAssessment(contact.getId()));
        update(contact);

        if (null == initiator.getEncountersInitiated()) {
            initiator.setEncountersInitiated(new TreeSet<>());
        }
        initiator.getEncountersInitiated().add(encounter);
        update(initiator);
    }

    @Override
    @Transactional
    public void updateEncounter(String contactId, String encounterId, EncounterDto dto) throws ConstraintViolation, NullDomainReference, InvalidApiRequestException {
        encounterService.updateEncounter(contactId, encounterId, dto);
    }


    @Override
    @Transactional
    public SortedSet<Encounter> getAllContactEncounters(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        SortedSet<Encounter> encounters = contact.getEncounters();

        if (encounters == null) {
            encounters = new TreeSet<>();
        }

        encounters.size();
        return encounters;
    }

    @Override
    @Transactional
    public SortedSet<Encounter> getAllContactEncountersInitiated(String contactId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        SortedSet<Encounter> encounters = contact.getEncountersInitiated();

        if (encounters == null) {
            encounters = new TreeSet<>();
        }

        encounters.size();
        return encounters;
    }

    @Override
    public int getUpdatedAssessment(String contactId) throws NullDomainReference {
        /*Sets assessment to most recent encounter assessment */
        Contact contact = findContact(contactId);
        if (null == contact.getEncounters() || contact.getEncounters().isEmpty()) {
            return contact.getAssessment();
        }

        return contact.getEncounters().first().getAssessment() == Encounter.DEFAULT_ASSESSMENT ? contact.getAssessment() :
                contact.getEncounters().first().getAssessment();
    }

    @Override
    public void updateNeedsFollowUp(String contactId, boolean followUp) throws NullDomainReference {
        Contact contact = findContact(contactId);
        contact.setNeedsFollowUp(followUp);
        update(contact);
    }

    @Override
    @Transactional
    public void removeEncounter(String contactId, String encounterId) throws NullDomainReference {
        Contact contact = findContact(contactId);
        Encounter encounter = encounterService.findById(encounterId);
        Contact initiator = encounter.getInitiator();

        contact.getEncounters().remove(encounter);
        contact.setAssessment(getUpdatedAssessment(contact.getId()));

        if (null != initiator) {
            initiator.getEncountersInitiated().remove(encounter);
            update(initiator);
        }

        update(contact);
    }

    @Override
    public void removeInitiator(String initiatorId, String encounterId) throws NullDomainReference {
        Contact initiator = findContact(initiatorId);
        Encounter encounter = encounterService.findById(encounterId);
        Contact contact = encounter.getContact();

        initiator.getEncountersInitiated().remove(encounter);
        contact.getEncounters().remove(encounter);

        update(initiator);
        update(contact);
    }

    @Override
    public void updateMemberInfo(String contactId, MemberInfo memberInfo) throws NullDomainReference {
        Contact contact = findContact(contactId);
        contact.setMemberInfo(memberInfo);
        update(contact);
    }

    @Override
    public void updateAssessment(String contactId, int assessment) throws NullDomainReference {
        Contact contact = findContact(contactId);
        contact.setAssessment(assessment);
        update(contact);
    }

    @Override
    public Contact findBySignInDto(SignInDto signInDto) throws NotFoundException {

        boolean hasEmail = signInDto.getEmail() != null && !signInDto.getEmail().isEmpty();
        boolean hasPhone = signInDto.getPhoneNumber() != null && !signInDto.getPhoneNumber().isEmpty();

        if (signInDto.getFirstName() == null || (!hasEmail && !hasPhone)) {
            throw NotFoundException.createException();
        }

        Contact contact = null;

        //Search by first name and email
        if (hasEmail) {
            contact = contactDao.findOneByFirstNameAndEmail(signInDto.getFirstName(), signInDto.getEmail());
            if (contact != null) {
                return contact;
            }
        }

        //Search by first name and phone1/phone2
        if (hasPhone) {
            contact = contactDao.findOneByFirstNameAndPhoneNumber1(signInDto.getFirstName(), signInDto.getPhoneNumber());
            if (contact != null) {
                return  contact;
            }
            contact = contactDao.findOneByFirstNameAndPhoneNumber2(signInDto.getFirstName(), signInDto.getPhoneNumber());
            if (contact != null) {
                return contact;
            }
        }

        //Otherwise, if nickname is provided ...
        if (signInDto.getNickName() != null && !signInDto.getNickName().isEmpty()) {
            //Search by nickname and email
            if (hasEmail) {
                contact = contactDao.findOneByNickNameAndEmail(signInDto.getNickName(), signInDto.getEmail());
                if (contact != null) {
                    return contact;
                }
            }

            //Search by nickname and phone1/phone2
            if (hasPhone) {
                contact = contactDao.findOneByNickNameAndPhoneNumber1(signInDto.getNickName(), signInDto.getPhoneNumber());
                if (contact != null) {
                    return  contact;
                }
                contact = contactDao.findOneByNickNameAndPhoneNumber2(signInDto.getNickName(), signInDto.getPhoneNumber());
                if (contact != null) {
                    return contact;
                }
            }
        }

        throw NotFoundException.createException();
    }

    @Override
    @Transactional
    public MemberInfo getContactMemberInfo(String id) throws NullDomainReference {
        Contact contact = findContact(id);
        MemberInfo memberInfo = contact.getMemberInfo();
        if (memberInfo == null) {
            return null;
        } else {
            memberInfo.getStatus();
            return memberInfo;
        }

    }

    private Contact findContact(String id) throws NullDomainReference {
        Contact contact = contactDao.findOne(id);
        if (contact == null) {
            throw new NullDomainReference.NullContact(id);
        }
        return contact;
    }
    private void refreshContactSustainerStatus(Contact contact) {
        boolean isCurrentSustainer = false;
        for (SustainerPeriod period: contact.getDonorInfo().getSustainerPeriods()) {
            if (period.getCancelDate() == null) {
                isCurrentSustainer = true;
                break;
            }
        }
        contact.getDonorInfo().setCurrentSustainer(isCurrentSustainer);
    }
}
