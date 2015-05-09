package edu.usm.it.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 3/12/15.
 */

public class ContactControllerTest extends WebAppConfigurationAware {


    @Autowired
    ContactService contactService;
    private Logger logger = LoggerFactory.getLogger(ContactControllerTest.class);

    @Autowired
    OrganizationService organizationService;

    @Autowired
    EventService eventService;


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
    public void testPostContact() throws Exception {
        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
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

        /*Event*/
        Event event = new Event();
        event.setDate(LocalDate.of(2015, 01, 01));
        event.setLocation("location");
        event.setNotes("notes");

        event.setAttendees(contacts);
        eventService.create(event);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        contact.setAttendedEvents(eventList);

        /*Donations*/
        Donation donation = new Donation();
        donation.setDate(LocalDate.of(2015, 01, 01));
        donation.setAmount(100);
        donation.setComment("comment");


        /*DonorInfo */
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.setDate(LocalDate.of(2015, 01, 01));

        List<Donation> donations = new ArrayList<>();
        donations.add(donation);
        donorInfo.setDonations(donations);
        contact.setDonorInfo(donorInfo);

        /*Member Info */
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setStatus(0);
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(true);
        contact.setMemberInfo(memberInfo);



        /*Organization*/
        Organization organization = new Organization();
        organization.setName("organization");
        organization.setMembers(contacts);
        organizationService.create(organization);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);

        contactService.update(contact);


        mockMvc.perform(get("/contacts/contact/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(contact.getId())))
                .andExpect(jsonPath("$.lastName", is(contact.getLastName())))
                .andExpect(jsonPath("$.streetAddress", is(contact.getStreetAddress())))
                .andExpect(jsonPath("$.aptNumber", is(contact.getAptNumber())))
                .andExpect(jsonPath("$.city", is(contact.getCity())))
                .andExpect(jsonPath("$.zipCode", is(contact.getZipCode())))
                .andExpect(jsonPath("$.email", is(contact.getEmail())))
                .andExpect(jsonPath("$.attendedEvents[0].id", is(contact.getAttendedEvents().get(0).getId())))
                .andExpect(jsonPath("$.donorInfo.donations[0].id", is(contact.getDonorInfo().getDonations().get(0).getId())))
                .andExpect(jsonPath("$.memberInfo.id", is(contact.getMemberInfo().getId())))
                .andExpect(jsonPath("$.organizations[0].id", is(contact.getOrganizations().iterator().next().getId())));

    }

}
