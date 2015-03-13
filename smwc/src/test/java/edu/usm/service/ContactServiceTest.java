package edu.usm.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by scottkimball on 3/12/15.
 */
public class ContactServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @After
    public void teardown () {
        contactService.deleteAll();
    }


    @Test
    public void testCreateAndFind () throws Exception {

        /*Basic info*/
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        contactService.create(contact);

        Contact fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertEquals(fromDb,contact);
        Assert.assertEquals(fromDb.getId(), contact.getId());
        Assert.assertEquals(fromDb.getLastName(), contact.getLastName());
        Assert.assertEquals(fromDb.getFirstName(), contact.getFirstName());
        Assert.assertEquals(fromDb.getEmail(), contact.getEmail());
        Assert.assertEquals(fromDb.getStreetAddress(), contact.getStreetAddress());
        Assert.assertEquals(fromDb.getAptNumber(), contact.getAptNumber());
        Assert.assertEquals(fromDb.getCity(), contact.getCity());
        Assert.assertEquals(fromDb.getZipCode(), contact.getZipCode());


        Contact contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");

        contactService.create(contact2);

        List<Contact> contacts = contactService.findAll();

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
        toDb.add(contact);
        toDb.add(contact2);

        contactService.updateList(toDb);

        List<Contact> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertEquals(contacts.size(),2);
    }

    public void testDelete() {

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

        contactService.delete(contact.getId());

        assertNull(contactService.findById(id));
        assertEquals(contactService.findAll().size(),0);

    }

}
