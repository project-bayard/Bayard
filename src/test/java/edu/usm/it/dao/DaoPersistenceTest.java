package edu.usm.it.dao;

import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.repository.*;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 2/22/15.
 */


public class DaoPersistenceTest extends WebAppConfigurationAware{

    @Autowired
    ContactDao contactDao;

    @Autowired
    EventDao eventDao;

    @Autowired
    DonorInfoDao donorInfoDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    EncounterDao encounterDao;

    @Autowired
    DateFormatConfig dateFormatConfig;

    @After
    public void tearDown() {
        encounterDao.deleteAll();
        contactDao.deleteAll();
        eventDao.deleteAll();
        organizationDao.deleteAll();

    }

    @Test
    @Transactional
    public void testDaoSave() throws Exception {


        /*Basic info*/
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");
        contact.setAssessment(1);
        contact.setOccupation("Machinist");
        contact.setPhoneNumber1("phone number");
        contact.setInterests("interests");


        /*Event*/
        Event event = new Event();
        String date = dateFormatConfig.formatDomainDate(LocalDate.of(2015, 01, 01));
        event.setDateHeld(date);
        event.setLocation("location");
        event.setNotes("notes");
        event.setName("name");
        eventDao.save(event);

        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        event.setAttendees(contacts);

        Set<Event> eventList = new HashSet<>();
        eventList.add(event);
        contact.setAttendedEvents(eventList);

        /*Donations*/
        Donation donation = new Donation();
        donation.setDateOfDeposit(LocalDate.of(2015, 01, 01));
        donation.setDateOfReceipt(LocalDate.of(2015, 01, 01));
        donation.setAmount(100);
        donation.setRestrictedToCategory("Category");
        donation.setBudgetItem("Budget Item 1");
        donation.setAnonymous(true);
        donation.setStandalone(false);

        /*DonorInfo*/
        DonorInfo donorInfo = new DonorInfo();

        donorInfo.addDonation(donation);
        contact.setDonorInfo(donorInfo);

        /*Member Info*/
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setStatus(0);
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(true);
        contact.setMemberInfo(memberInfo);

        /*Organization*/
        Organization organization = new Organization();
        organization.setName("organization");
        organization.setMembers(contacts);
        organization.setStreetAddress("123 Organizational Lane");
        organization.setCity("Portland");
        organization.setState("ME");
        organization.setZipCode("04103");
        organization.setPhoneNumber("123-456-7890");
        organization.setPrimaryContactName("Theo McCeo");
        organization.setDescription("A very good organization");
        organizationDao.save(organization);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);

         /*Encounters*/
        Encounter encounter = new Encounter();
        encounter.setAssessment(0);
        encounter.setContact(contact);
        encounter.setEncounterDate(LocalDate.now().toString());
        encounter.setNotes("Notes");

        EncounterType encounterType = new EncounterType("CALL");

        encounter.setType(encounterType.getName());
        contact.getEncounters().add(encounter);
        contactDao.save(contact);

        Contact fromDb = contactDao.findOne(contact.getId());

        /*Basic contact info*/
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(), contact.getId());
        assertEquals(fromDb.getLastName(),contact.getLastName());
        assertEquals(fromDb.getFirstName(),contact.getFirstName());
        assertEquals(fromDb.getEmail(),contact.getEmail());
        assertEquals(fromDb.getStreetAddress(), contact.getStreetAddress());
        assertEquals(fromDb.getAptNumber(),contact.getAptNumber());
        assertEquals(fromDb.getCity(),contact.getCity());
        assertEquals(fromDb.getZipCode(),contact.getZipCode());

        /*Event info*/
        assertNotNull(fromDb.getAttendedEvents());
        Event fromEventDao = eventDao.findOne(event.getId());
        assertEquals(fromEventDao.getId(), event.getId());
        Set<Contact> attendees = fromEventDao.getAttendees();
        assertNotNull(attendees);

        Contact attendee = eventDao.findOne(event.getId()).getAttendees().iterator().next();
        assertNotNull(attendee);
        assertEquals(attendee.getId(), contact.getId());

        /*Donor Info*/
        Donation fromDbDonation = donorInfoDao.findOne(donorInfo.getId()).getDonations().iterator().next();
        assertEquals(fromDbDonation.getId(),donation.getId());
        assertEquals(fromDbDonation.isAnonymous(),donation.isAnonymous());
        assertEquals(fromDbDonation.isStandalone(),donation.isStandalone());
        assertEquals(fromDbDonation.getBudgetItem(),donation.getBudgetItem());
        assertEquals(fromDbDonation.getRestrictedToCategory(),donation.getRestrictedToCategory());
        assertEquals(fromDbDonation.getDateOfDeposit(),donation.getDateOfDeposit());
        assertEquals(fromDbDonation.getDateOfReceipt(),donation.getDateOfReceipt());

        /*Member Info*/
        assertEquals(contactDao.findOne(contact.getId()).getMemberInfo().getStatus(),memberInfo.getStatus());

        /*Organization*/
        Organization fromDbOrg = organizationDao.findOne(organization.getId());
        assertNotNull(fromDbOrg);
        assertEquals(organizationDao.findOne(organization.getId()).getId(),organization.getId());
        assertEquals(organizationDao.findOne(organization.getId()).getDescription(),organization.getDescription());
        assertEquals(organizationDao.findOne(organization.getId()).getMembers().iterator().next().getId(),contact.getId());

        /*Encounters*/
        assertNotNull(contact.getEncounters());
        assertEquals(contact.getEncounters().first().getAssessment(), encounter.getAssessment());

    }

    @Test
    public void testFilterByInitiator() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setInitiator(true);
        Contact contact1 = new Contact();
        contact1.setFirstName("Second");
        contact1.setInitiator(true);
        contact1.setDeleted(true);
        contactDao.save(contact1);
        contactDao.save(contact);

        Set<Contact> contacts = contactDao.findAllInitiators();
        assertNotNull(contacts);
        assertEquals(contacts.size(),1);
        Contact fromDb = contacts.iterator().next();
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(),contact.getId());


    }


}
