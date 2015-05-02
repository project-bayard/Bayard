package edu.usm.it.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by scottkimball on 3/12/15.
 */

public class ContactControllerTest extends WebAppConfigurationAware {


    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;


    private Contact contact;

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
    }

    @After
    public void teardown () {
        contactService.deleteAll();
    }


    @Test
    @Transactional
    public void testGetAllContacts () throws Exception {


        contactService.create(contact);
        String id = contact.getId();

        mockMvc.perform(get("/contacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    @Transactional
    public void testGetContact() throws Exception {


        contactService.create(contact);
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);

        /*Event
        Event event = new Event();
        event.setDate(LocalDate.of(2015, 01, 01));
        event.setLocation("location");
        event.setNotes("notes");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        event.setAttendees(contacts);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        contact.setAttendedEvents(eventList);

        */

        /*Donations
        Donation donation = new Donation();
        donation.setDate(LocalDate.of(2015, 01, 01));
        donation.setAmount(100);
        donation.setComment("comment");

        */



        /*DonorInfo
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.setContact(contact);
        donorInfo.setDate(LocalDate.of(2015, 01, 01));



        List<Donation> donations = new ArrayList<>();
        donations.add(donation);
        donorInfo.setDonations(donations);
        contact.setDonorInfo(donorInfo);

        */

        /*Member Info
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setContact(contact);
        memberInfo.setStatus(0);
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(true);
        contact.setMemberInfo(memberInfo);

        */

        /*Organization*/



        Organization organization = new Organization();
        organization.setName("organization");
        organization.setMembers(contacts);

        organizationService.create(organization);


        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);


        contactService.update(contact);


        String id = contact.getId();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate4Module());


        mockMvc.perform(get("/contacts/contact/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


}
