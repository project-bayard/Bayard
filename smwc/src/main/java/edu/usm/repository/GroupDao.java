package edu.usm.repository;

import edu.usm.domain.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Created by andrew on 10/8/15.
 */
@Repository
public interface GroupDao extends CrudRepository<Group, String>{

    @Override
    HashSet<Group> findAll();

}
