package edu.usm.repository;

import edu.usm.domain.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for Groups
 */
@Repository
public interface GroupDao extends CrudRepository<Group, String>{

    /**
     * Returns all existing groups.
     * @return {@link java.util.Set} of {@link Group}
     */
    @Override
    HashSet<Group> findAll();

    /**
     * Finds a group by its name, if it exists.
     * @param groupName
     * @return {@link Group}
     */
    Group findByGroupName(String groupName);

}
