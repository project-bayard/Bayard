package edu.usm.service.impl;

import com.google.common.collect.Lists;
import edu.usm.domain.Contact;
import edu.usm.repository.ContactDao;
import edu.usm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by scottkimball on 3/12/15.
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao dao;



    @Override
    public Contact findById(long id) {
        return dao.findOne(id);
    }

    @Override
    public List<Contact> findAll() {

        return  Lists.newArrayList(dao.findAll());
    }

    @Override
    public void delete(long id) {
        dao.delete(id);

    }

    @Override
    public void update(Contact contact) {
        dao.save(contact);
    }

    @Override
    public void updateList(List<Contact> contacts) {
        dao.save(contacts);
    }

    @Override
    public void create(Contact contact) {
        update(contact);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
