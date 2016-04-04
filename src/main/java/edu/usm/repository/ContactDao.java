package edu.usm.repository;

import edu.usm.domain.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Respository for {@link Contact}
 */

@Repository
public interface ContactDao extends CrudRepository<Contact, String> {

    /**
     * Returns all existing Contacts
     * @return {@link Set} of {@link Contact}
     */
    @Override
    HashSet<Contact> findAll();

    /**
     * Returns all Contacts who can initiate encounters.
     * @return A {@link Set} of {@link Contact}
     */
    @Query("select c from contact as c where initiator = 'true'")
    HashSet<Contact> findAllInitiators();

    /**
     * Returns all contacts with a given first name.
     * @param firstName The first name of the contact.
     * @return {@link Set} of {@link Contact}
     */
    Set<Contact> findByFirstName(String firstName);

    /**
     * Finds a contact by firstName and email if it exists
     * @param firstName
     * @param email
     * @return {@link Contact}
     */
    Contact findOneByFirstNameAndEmail(String firstName, String email);

    /**
     * Finds a contact by firstName and phoneNumber1 if it exists.
     * @param firstName
     * @param phoneNumber1
     * @return {@link Contact}
     */
    Contact findOneByFirstNameAndPhoneNumber1(String firstName, String phoneNumber1);

    /**
     * Finds a contact by firstName and phoneNumber2 if it exists.
     * @param firstName
     * @param phoneNumber2
     * @return {@link Contact}
     */
    Contact findOneByFirstNameAndPhoneNumber2(String firstName, String phoneNumber2);
}
