package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NotFoundException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.SignInDto;
import edu.usm.repository.SustainerPeriodDao;
import edu.usm.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.*;


/**
 * Created by scottkimball on 3/12/15.
 */
public class ContactServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CommitteeService committeeService;

    @Autowired
    DonationService donationService;

    @Autowired
    SustainerPeriodDao sustainerPeriodDao;

    @Autowired
    EncounterTypeService encounterTypeService;

    private Contact contact;
    private Contact contact2;
    private Organization organization;
    private Committee committee;
    private Donation donation;
    private SustainerPeriod sustainerPeriod;

    @Before
    public void setup() {
        contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");
        contact.setNeedsFollowUp(false);
        contact.setPhoneNumber1("123-456-7891");
        contact.setPhoneNumber2("234-567-8901");

        contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");
        contact2.setInitiator(true);
        contact2.setPhoneNumber1("890-121-3231");

        donation = new Donation();
        donation.setAmount(200);
        donation.setDateOfDeposit(LocalDate.now());
        donation.setDateOfDeposit(LocalDate.of(2015, 1, 1));

        sustainerPeriod = new SustainerPeriod();
        sustainerPeriod.setPeriodStartDate(LocalDate.of(2015, 1, 1));
        sustainerPeriod.setCancelDate(LocalDate.now());
        sustainerPeriod.setSentIRSLetter(true);
        sustainerPeriod.setMonthlyAmount(20);

        organization = new Organization();
        organization.setName("organization");
        committee = new Committee();
        committee.setName("committee");
    }

    @After
    public void teardown() {
        contactService.deleteAll();
        organizationService.deleteAll();
        committeeService.deleteAll();
        donationService.deleteAll();
        encounterTypeService.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndFind () throws Exception {

        contactService.create(contact);
        contactService.create(contact2);

        Contact fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertEquals(fromDb, contact);
        assertEquals(fromDb.getId(), contact.getId());
        assertEquals(fromDb.getLastName(), contact.getLastName());
        assertEquals(fromDb.getFirstName(), contact.getFirstName());
        assertEquals(fromDb.getEmail(), contact.getEmail());
        assertEquals(fromDb.getStreetAddress(), contact.getStreetAddress());
        assertEquals(fromDb.getAptNumber(), contact.getAptNumber());
        assertEquals(fromDb.getCity(), contact.getCity());
        assertEquals(fromDb.getZipCode(), contact.getZipCode());


        Set<Contact> contacts = contactService.findAll();
        assertEquals(contacts.size(), 2);
        assertTrue(contacts.contains(contact));
        assertTrue(contacts.contains(contact2));


    }

    @Test
    public void testFindAllAndUpdateList () throws Exception {

        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        Contact contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");

        List<Contact> toDb = new ArrayList<>();

        contactService.create(contact);
        contactService.create(contact2);
        Set<Contact> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertEquals(contacts.size(), 2);
    }

    @Test(expected = NullDomainReference.NullContact.class)
    public void testDelete () throws NullDomainReference, ConstraintViolation{
        contactService.create(contact);
        contactService.create(contact2);
        organizationService.create(organization);
        contactService.addContactToOrganization(contact.getId(), organization.getId());
        contactService.addContactToOrganization(contact2.getId(), organization.getId());
        contactService.delete(contact.getId());
        Organization fromDb = organizationService.findById(organization.getId());

        assertNotNull(fromDb);
        assertFalse(fromDb.getMembers().contains(contact));
        assertTrue(fromDb.getMembers().contains(contact2));
        assertNull(contactService.findById(contact.getId())); // Should throw NullDomainReference.NullContact
    }

    @Test
    public void testDeleteContactsWithEncountersInitiatorFirst() throws Exception {
        contactService.create(contact);
        contactService.create(contact2);
        createEncounter();

        contactService.delete(contact2.getId());
        contactService.delete(contact.getId());

    }

    private void createEncounter() throws Exception{

        EncounterType type = encounterTypeService.findByName("Test Type");
        if (null == type) {
            type = new EncounterType("Test Type");
            encounterTypeService.create(type);
        }

        EncounterDto encounterDto = new EncounterDto();
        encounterDto.setEncounterDate("2015-01-10");
        encounterDto.setInitiatorId(contact2.getId());
        encounterDto.setType(type.getId());
        contactService.addEncounter(contact.getId(), encounterDto);

        contact = contactService.findById(contact.getId());
        assertTrue(contactService.getAllContactEncounters(contact.getId()).size() == 1);
    }

    @Test
    public void testDeleteContactsWithEncountersSubjectFirst() throws Exception {
        contactService.create(contact);
        contactService.create(contact2);
        createEncounter();

        contact = contactService.findById(contact.getId());
        assertTrue(contactService.getAllContactEncounters(contact.getId()).size() == 1);

        contactService.delete(contact.getId());
        contactService.delete(contact2.getId());

    }

    @Test
    public void testDeleteContactsMultipleEncounters() throws Exception {
        contactService.create(contact);
        contactService.create(contact2);

        EncounterType type = encounterTypeService.findByName("Test Type");
        if (null == type) {
            type = new EncounterType("Test Type");
            encounterTypeService.create(type);
        }

        EncounterDto encounterDto = new EncounterDto();
        encounterDto.setEncounterDate("2015-01-10");
        encounterDto.setInitiatorId(contact2.getId());
        encounterDto.setType(type.getId());
        contactService.addEncounter(contact.getId(), encounterDto);

        contact = contactService.findById(contact.getId());
        assertTrue(contactService.getAllContactEncounters(contact.getId()).size() == 1);

        encounterDto = new EncounterDto();
        encounterDto.setEncounterDate("2013-07-07");
        encounterDto.setInitiatorId(contact2.getId());
        encounterDto.setType(type.getId());
        contactService.addEncounter(contact.getId(), encounterDto);

        contact = contactService.findById(contact.getId());
        assertTrue(contactService.getAllContactEncounters(contact.getId()).size() == 2);

        contactService.delete(contact.getId());
        contactService.delete(contact2.getId());
    }

    @Test
    public void testAddAndRemoveContactFromOrganization () throws Exception {
        contactService.create(contact);
        organizationService.create(organization);
        contactService.addContactToOrganization(contact.getId(), organization.getId());

        Contact fromDb = contactService.findById(contact.getId());
        Set<Organization> organizations = contactService.getAllContactOrganizations(fromDb.getId());
        assertNotNull(fromDb);
        assertNotNull(organizations);
        assertTrue(organizations.contains(organization));

        contactService.removeContactFromOrganization(contact.getId(), organization.getId());
        fromDb = contactService.findById(contact.getId());
        Set<Organization> orgsFromDb = contactService.getAllContactOrganizations(fromDb.getId());
        Organization orgFromDb = organizationService.findById(organization.getId());

        assertNotNull(fromDb);
        assertNotNull(fromDb.getOrganizations());
        assertNotNull(orgFromDb);
        assertNotNull(orgFromDb.getMembers());
        assertFalse(orgFromDb.getMembers().contains(contact));
        assertFalse(orgsFromDb.contains(organization));
    }

    @Test
    public void testAddAndRemoveContactFromCommittee () throws Exception {
        contactService.create(contact);
        committeeService.create(committee);

        contactService.addContactToCommittee(contact.getId(), committee.getId());

        Contact fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        Set<Committee> committees = contactService.getAllContactCommittees(fromDb.getId());
        assertNotNull(committees);
        assertTrue(committees.contains(committee));

        contactService.removeContactFromCommittee(contact.getId(), committee.getId());

        fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        Set<Committee> committeesFromDb = contactService.getAllContactCommittees(fromDb.getId());
        assertNotNull(committeesFromDb);
        assertFalse(committeesFromDb.contains(committee));

        Committee committeeFromDb = committeeService.findById(committee.getId());
        assertNotNull(committeeFromDb);
        assertNotNull(committeeFromDb.getMembers());
        assertFalse(committeeFromDb.getMembers().contains(contact));

    }

    @Test
    public void testUpdateBasicDetails () throws Exception {
        contactService.create(contact);

        Contact details = new Contact();
        details.setFirstName("newFirstName");
        details.setLastName("newLastName");
        details.setStreetAddress("123 Fake St");
        details.setAptNumber("# 4");
        details.setCity("Portland");
        details.setZipCode("04101");
        details.setEmail("email@gmail.com");

        contactService.updateBasicDetails(contact.getId(), details);

        Contact fromDb = contactService.findById(contact.getId());
        assertEquals(fromDb.getFirstName(), details.getFirstName());
        assertEquals(fromDb.getLastName(), details.getLastName());
        assertEquals(fromDb.getStreetAddress(), details.getStreetAddress());

    }

    @Test
    public void testAddEncounter () throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(contact2);

        EncounterDto dto = new EncounterDto();
        dto.setEncounterDate("2012-01-01");
        dto.setNotes("Notes!");
        dto.setInitiatorId(initiatorId);
        EncounterType encounterType = new EncounterType("CALL");
        encounterTypeService.create(encounterType);

        dto.setType(encounterType.getId());
        contactService.addEncounter(contact.getId(), dto);

        Contact fromDb = contactService.findById(contact.getId());

        SortedSet<Encounter> encounters = contactService.getAllContactEncounters(contact.getId());

        assertNotNull(fromDb.getEncounters());
        assertEquals(encounters.first().getContact().getId(), contact.getId());
        assertEquals(encounters.first().getInitiator().getId(), contact2.getId());
        assertEquals(encounters.first().getAssessment(), dto.getAssessment());

        Contact initiatorFromDb = contactService.findById(initiatorId);
        assertEquals(1, contactService.getAllContactEncountersInitiated(initiatorFromDb.getId()).size());

    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateEncounterNullDate() throws ConstraintViolation, NullDomainReference {
        String id = contactService.create(contact);
        contactService.create(contact2);

        EncounterDto dto = new EncounterDto();
        dto.setNotes("Notes!");
        dto.setInitiatorId(contact2.getId());
        EncounterType encounterType = new EncounterType("CALL");
        encounterTypeService.create(encounterType);
        dto.setType(encounterType.getId());
        contactService.addEncounter(contact.getId(), dto);

    }

    @Test(expected = NullDomainReference.class)
    public void testCreateEncounterNullType() throws ConstraintViolation, NullDomainReference {
        String id = contactService.create(contact);
        contactService.create(contact2);

        EncounterDto dto = new EncounterDto();
        dto.setNotes("Notes!");
        dto.setInitiatorId(contact2.getId());
        contactService.addEncounter(contact.getId(), dto);

    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateEncounterNotInitiator() throws ConstraintViolation, NullDomainReference {
        String id = contactService.create(contact);
        contact2.setInitiator(false);
        contactService.create(contact2);

        EncounterDto dto = new EncounterDto();
        dto.setNotes("Notes!");
        EncounterType encounterType = new EncounterType("CALL");
        encounterTypeService.create(encounterType);
        dto.setType(encounterType.getName());
        dto.setInitiatorId(contact2.getId());
        contactService.addEncounter(contact.getId(), dto);

    }

    @Test
    public void testAddMultipleEncounters() throws Exception {
        String id = contactService.create(contact);
        contactService.create(contact2);

        EncounterDto firstEncounter = new EncounterDto();
        firstEncounter.setEncounterDate("2014-01-01");
        firstEncounter.setNotes("Notes!");
        firstEncounter.setAssessment(10);
        firstEncounter.setRequiresFollowUp(false);
        firstEncounter.setInitiatorId(contact2.getId());

        EncounterType encounterType = new EncounterType("CALL");
        encounterTypeService.create(encounterType);
        firstEncounter.setType(encounterType.getId());

        contactService.addEncounter(contact.getId(), firstEncounter);

        EncounterDto secondEncounter = new EncounterDto();
        int mostRecentAssessment = 5;
        boolean mostRecentFollowUpIndicator = true;
        secondEncounter.setEncounterDate("2015-01-01");
        secondEncounter.setAssessment(mostRecentAssessment);
        secondEncounter.setNotes("More notes!");
        secondEncounter.setRequiresFollowUp(mostRecentFollowUpIndicator);
        secondEncounter.setType(encounterType.getId());
        secondEncounter.setInitiatorId(contact2.getId());

        contactService.addEncounter(contact.getId(), secondEncounter);

        EncounterDto thirdEncounter = new EncounterDto();
        thirdEncounter.setEncounterDate("2013-01-01");
        thirdEncounter.setRequiresFollowUp(false);
        thirdEncounter.setAssessment(7);
        thirdEncounter.setInitiatorId(contact2.getId());
        thirdEncounter.setType(encounterType.getId());

        contactService.addEncounter(contact.getId(), thirdEncounter);

        Contact fromDb = contactService.findById(id);
        assertEquals(mostRecentAssessment, fromDb.getAssessment());
        assertEquals(mostRecentFollowUpIndicator, fromDb.needsFollowUp());

    }

    @Test
    public void testUpdateFollowUp() throws Exception {
        String id = contactService.create(contact);
        Contact fromDb = contactService.findById(id);
        assertFalse(fromDb.needsFollowUp());
        contactService.updateNeedsFollowUp(contact.getId(), true);
        fromDb = contactService.findById(id);
        assertTrue(fromDb.needsFollowUp());

    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactDuplicateNameEmail() throws ConstraintViolation, NullDomainReference {
        contactService.create(contact);
        contact2.setFirstName(contact.getFirstName());
        contact2.setEmail(contact.getEmail());
        contactService.create(contact2);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactDuplicateNamePhone() throws ConstraintViolation, NullDomainReference {
        contactService.create(contact);
        contact2.setFirstName(contact.getFirstName());
        contact2.setPhoneNumber1(contact.getPhoneNumber1());
        contactService.create(contact2);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactNullFirstName() throws Exception {
        contact.setFirstName(null);
        contactService.create(contact);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactEmptyFirstName() throws Exception {
        contact.setFirstName("");
        contactService.create(contact);
    }

    @Test
    public void testCreateContactNullPhoneNumber() throws Exception {
        contact.setPhoneNumber1(null);
        contactService.create(contact);
        assertTrue(contactService.findById(contact.getId()).getPhoneNumber1() == null);
    }

    @Test
    public void testCreateContactEmptyPhoneNumber() throws Exception {
        contact.setPhoneNumber1("");
        contactService.create(contact);
        assertTrue(contactService.findById(contact.getId()).getPhoneNumber1() == null);
    }

    @Test
    public void testCreateContactNullEmail() throws Exception {
        contact.setEmail(null);
        contactService.create(contact);
        assertTrue(contactService.findById(contact.getId()).getEmail() == null);
    }

    @Test
    public void testCreateContactEmptyEmail() throws Exception {
        contact.setEmail("");
        contactService.create(contact);
        assertTrue(contactService.findById(contact.getId()).getEmail() == null);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactEmptyEmailNullPhoneNumber() throws Exception {
        contact.setEmail("");
        contact.setPhoneNumber1(null);
        contactService.create(contact);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactNullEmailEmptyPhoneNumber() throws Exception {
        contact.setEmail(null);
        contact.setPhoneNumber1("");
        contactService.create(contact);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactEmptyEmailEmptyPhoneNumber() throws Exception {
        contact.setEmail("");
        contact.setPhoneNumber1("");
        contactService.create(contact);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateContactNullEmailNullPhoneNumber() throws Exception {
        contact.setEmail(null);
        contact.setPhoneNumber1(null);
        contactService.create(contact);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateContactDuplicateNameEmail() throws ConstraintViolation, NullDomainReference {
        contactService.create(contact);
        contactService.create(contact2);

        Contact details = new Contact();
        details.setFirstName(contact.getFirstName());
        details.setEmail(contact.getEmail());

        contactService.updateBasicDetails(contact2.getId(), details);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateContactDuplicateNamePhone() throws ConstraintViolation, NullDomainReference {
        contactService.create(contact);
        contactService.create(contact2);

        Contact details = new Contact();
        details.setFirstName(contact.getFirstName());
        details.setPhoneNumber1(contact.getPhoneNumber1());

        contactService.updateBasicDetails(contact2.getId(), details);
    }

    @Test(expected = NotFoundException.class)
    public void testFindByFirstLastEmailPhone() throws Exception {
        contactService.create(contact);

        SignInDto dto = new SignInDto(contact.getFirstName(),contact.getLastName(), contact.getEmail(),
                contact.getPhoneNumber1());

        Contact fromDb = contactService.findByFirstEmailPhone(dto);
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(), contact.getId());

        dto.setEmail(null);
        fromDb = contactService.findByFirstEmailPhone(dto);
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(), contact.getId());

        dto.setEmail(contact.getEmail());
        dto.setPhoneNumber(null);
        fromDb = contactService.findByFirstEmailPhone(dto);
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(), contact.getId());

        dto.setEmail(null);
        dto.setPhoneNumber(contact.getPhoneNumber2());
        fromDb = contactService.findByFirstEmailPhone(dto);
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(), contact.getId());

        dto.setPhoneNumber(null);
        fromDb = contactService.findByFirstEmailPhone(dto);

        dto.setPhoneNumber(contact.getPhoneNumber1());
        dto.setFirstName(null);
        fromDb = contactService.findByFirstEmailPhone(dto);

    }
    public void testAddDonation() throws Exception {
        contactService.create(contact);
        contactService.addDonation(contact.getId(), DtoTransformer.fromEntity(donation));
        contact = contactService.findById(contact.getId());
        assertNotNull(contact.getDonorInfo());
        assertFalse(contact.getDonorInfo().getDonations().isEmpty());
    }

    @Test
    public void testRemoveDonation() throws Exception {
        contactService.create(contact);
        contactService.addDonation(contact.getId(), DtoTransformer.fromEntity(donation));
        contact = contactService.findById(contact.getId());
        donation = contactService.getDonorInfo(contact.getId()).getDonations().iterator().next();
        assertNotNull(donation);

        contactService.removeDonation(contact.getId(), donation.getId());
        contact = contactService.findById(contact.getId());
        assertTrue(contactService.getDonorInfo(contact.getId()).getDonations().isEmpty());

        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
    }

    @Test
    public void testFindContactWithDonation() throws Exception {
        contactService.create(contact);
        contactService.addDonation(contact.getId(), DtoTransformer.fromEntity(donation));

        contact = contactService.findById(contact.getId());
        donation = contactService.getDonorInfo(contact.getId()).getDonations().iterator().next();
        Contact withDonation = contactService.findContactWithDonation(donation);
        assertEquals(contact, withDonation);

    }

    @Test
    public void testCreateSustainerPeriod() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact.getId(), DtoTransformer.fromEntity(sustainerPeriod));

        contact = contactService.findById(contact.getId());
        assertNotNull(contact.getDonorInfo());
        assertFalse(contactService.getDonorInfo(contact.getId()).getSustainerPeriods().isEmpty());
    }

    @Test
    public void testUpdateSustainerPeriod() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact.getId(), DtoTransformer.fromEntity(sustainerPeriod));
        contact = contactService.findById(contact.getId());
        sustainerPeriod = contactService.getDonorInfo(contact.getId()).getSustainerPeriods().iterator().next();

        int newMonthlyAmount = sustainerPeriod.getMonthlyAmount() + 50;
        sustainerPeriod.setMonthlyAmount(newMonthlyAmount);
        contactService.updateSustainerPeriod(contact.getId(), sustainerPeriod.getId(), DtoTransformer.fromEntity(sustainerPeriod));

        contact = contactService.findById(contact.getId());
        sustainerPeriod = contactService.getDonorInfo(contact.getId()).getSustainerPeriods().iterator().next();
        assertEquals(newMonthlyAmount, sustainerPeriod.getMonthlyAmount());
    }

    @Test
    public void testDeleteSustainerPeriod() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact.getId(), DtoTransformer.fromEntity(sustainerPeriod));
        contact = contactService.findById(contact.getId());
        sustainerPeriod = contactService.getDonorInfo(contact.getId()).getSustainerPeriods().iterator().next();

        contactService.deleteSustainerPeriod(contact.getId(), sustainerPeriod.getId());
        contact = contactService.findById(contact.getId());
        assertTrue(contactService.getDonorInfo(contact.getId()).getSustainerPeriods().isEmpty());
        assertNull(sustainerPeriodDao.findOne(sustainerPeriod.getId()));
    }

    @Test
    public void testGetAllCurrentSustainers() throws Exception {
        contactService.create(contact);
        contactService.create(contact2);
        contactService.createSustainerPeriod(contact.getId(), DtoTransformer.fromEntity(sustainerPeriod));
        SustainerPeriod openSustainerPeriod = new SustainerPeriod(contact.getDonorInfo(), LocalDate.now(), 10);
        contactService.createSustainerPeriod(contact2.getId(), DtoTransformer.fromEntity(openSustainerPeriod));

        contact = contactService.findById(contact.getId());
        contact2 = contactService.findById(contact2.getId());
        Set<Contact> sustainers = contactService.findAllCurrentSustainers();
        assertTrue(sustainers.contains(contact2));
        assertFalse(sustainers.contains(contact));
    }

}