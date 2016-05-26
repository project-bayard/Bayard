package edu.usm.repository;

import com.mysema.query.types.Predicate;
import edu.usm.domain.Contact;
import edu.usm.domain.DonorInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Respository for {@link Contact}
 */

@Repository
public interface ContactDao extends CrudRepository<Contact, String> , QueryDslPredicateExecutor<Contact> {

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

    /**
     * Finds a contact by nickName and email if it exists
     * @param nickName
     * @param email
     * @return {@link Contact}
     */
    Contact findOneByNickNameAndEmail(String nickName, String email);

    /**
     * Finds a contact by nickName and phoneNumber1 if it exists.
     * @param nickName
     * @param phoneNumber1
     * @return {@link Contact}
     */
    Contact findOneByNickNameAndPhoneNumber1(String nickName, String phoneNumber1);

    /**
     * Finds a contact by nickName and phoneNumber2 if it exists.
     * @param nickName
     * @param phoneNumber2
     * @return {@link Contact}
     */
    Contact findOneByNickNameAndPhoneNumber2(String nickName, String phoneNumber2);

    Set<Contact> findByDonorInfoCurrentSustainer(boolean status);

    Contact findOneByDonorInfo(DonorInfo donorInfo);

    @Override
    Set<Contact> findAll(Predicate predicate);
}
