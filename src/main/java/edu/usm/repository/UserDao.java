package edu.usm.repository;

import edu.usm.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for users
 */
@Repository
public interface UserDao extends CrudRepository<User, Long> {

    /**
     * Finds a user by its email, if it exists.
     * @param email
     * @return {@link User}
     */
    User findOneByEmail(String email);

    /**
     * Returns all existing users.
     * @return {@link List} of {@link User}
     */
    List<User> findAll();
}
