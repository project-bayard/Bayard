package edu.usm.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.config.WebSecurityConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.repository.ContactDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 2/22/15.
 */


public class DaoPersistenceTest extends WebAppConfigurationAware{



    @Autowired
    ContactDao contactDao;

    @Test
    public void TestDao () throws Exception {


        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setAddress("123 Fake St");
        contact.setEmail("email@gmail.com");

        contactDao.save(contact);

        Contact fromDb = contactDao.findOne(contact.getId());

        assertNotNull(fromDb);
    }





}
