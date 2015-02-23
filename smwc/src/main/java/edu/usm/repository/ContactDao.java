package edu.usm.repository;

import edu.usm.domain.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface ContactDao extends CrudRepository<Contact, Long> {
}
