package edu.usm.controller;


import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.service.ContactService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by scottkimball on 3/12/15.
 */

public class ContactControllerTest extends WebAppConfigurationAware {


    @Autowired
    ContactService contactService;


    @Before
    public void setup() {

    }

    @After
    public void teardown () {
        contactService.deleteAll();
    }


    @Test
    @Transactional
    public void testGetAllContacts () throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");
        contact.setId(1234);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);

        contactService.create(contact);
        long id = contact.getId();

        mockMvc.perform(get("/contacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    @Transactional
    public void testGetContact() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        contactService.create(contact);
        long id = contact.getId();

        mockMvc.perform(get("/contacts/contact/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


}
