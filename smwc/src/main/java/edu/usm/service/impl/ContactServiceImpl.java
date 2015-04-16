package edu.usm.service.impl;

import com.google.common.collect.Lists;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.repository.ContactDao;
import edu.usm.repository.OrganizationDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by scottkimball on 3/12/15.
 */
@Service
public class ContactServiceImpl extends BasicService implements ContactService {

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private OrganizationDao organizationDao;

    private Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);


    @Override
    public Contact findById(String id) {
        logger.debug("Finding contact with ID: " + id);
        return contactDao.findOne(id);
    }

    @Override
    public List<Contact> findAll() {
        logger.debug("Finding all Contacts");
        return  Lists.newArrayList(contactDao.findAll());
    }

    @Override
    public void delete(Contact contact) {
        logger.debug("Deleting contact with ID: " + contact.getId() );
        logger.debug("Time: " + LocalDateTime.now());

        updateLastModified(contact);

        /*Remove references to */
        if (contact.getOrganizations() != null) {
            for(Organization organization : contact.getOrganizations()) {
                organization.getMembers().remove(contact);
                organizationDao.save(organization);
            }
        }


        contactDao.delete(contact);
    }

    @Override
    public void update(Contact contact) {
        logger.debug("Updating contact with ID: " + contact.getId());
        logger.debug("Time: " + LocalDateTime.now());
        updateLastModified(contact);
        contactDao.save(contact);
    }



    @Override
    public void create(Contact contact) {
        logger.debug("Creating contact with ID: " + contact.getId());
        logger.debug("Time: " + LocalDateTime.now());
        contactDao.save(contact);

    }

    @Override
    public void deleteAll() {

        logger.debug("Deleting all contacts.");
        List<Contact> contacts = findAll();
        contacts.stream().forEach(this::delete);
    }
}
