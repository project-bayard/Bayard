package edu.usm.repository;

import edu.usm.domain.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface ContactDao extends CrudRepository<Contact, String> {

    @Override
    HashSet<Contact> findAll();

    @Query("select c from contact as c where initiator = 'true'")
    HashSet<Contact> findAllInitiators();

    Set<Contact> findByFirstName(String firstName);

    Contact findOneByFirstNameAndEmail(String firstName, String email);

    Contact findOneByFirstNameAndPhoneNumber1(String firstName, String phoneNumber1);

    Contact findOneByFirstNameAndPhoneNumber2(String firstName, String phoneNumber2);


}
