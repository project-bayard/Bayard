package edu.usm.repository;

import edu.usm.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by scottkimball on 7/17/15.
 */
@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findOneByEmail(String email);
    List<User> findAll();
}
