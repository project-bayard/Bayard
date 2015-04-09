package edu.usm.service.impl;

import com.google.common.collect.Lists;
import edu.usm.domain.Contact;
import edu.usm.repository.ContactDao;
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
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao dao;
    private Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);


    @Override
    public Contact findById(String id) {
        logger.debug("Finding contact with ID: " + id);
        return dao.findById(id);
    }

    @Override
    public List<Contact> findAll() {
        logger.debug("Finding all Contacts");
        return  Lists.newArrayList(dao.findAll());
    }

    @Override
    public void delete(String id) {
        logger.debug("Deleting contact with ID: " + id );
        logger.debug("Time: " + LocalDateTime.now());
        dao.deleteById(id);
    }

    @Override
    public void update(Contact contact) {
        logger.debug("Updating contact with ID: " + contact.getId());
        logger.debug("Time: " + LocalDateTime.now());
        dao.save(contact);
    }

    @Override
    public void updateList(List<Contact> contacts) {
        dao.save(contacts);
    }

    @Override
    public void create(Contact contact) {
        update(contact);
        logger.debug("Creating contact with ID: " + contact.getId());
        logger.debug("Time: " + LocalDateTime.now());

    }

    @Override
    public void deleteAll() {

        logger.debug("Deleting all contacts.");
        logger.debug("Time: " + LocalDateTime.now());
        dao.deleteAll();
    }
}
