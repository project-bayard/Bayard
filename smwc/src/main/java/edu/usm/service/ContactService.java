package edu.usm.service;

import edu.usm.domain.Contact;

import java.util.List;

/**
 * Created by scottkimball on 3/12/15.
 */

public interface ContactService {

    Contact findById (String id);
    List<Contact> findAll();
    void delete (String id);
    void update (Contact contact);
    void updateList (List<Contact> contacts);
    void create(Contact contact);
    void deleteAll();

}
