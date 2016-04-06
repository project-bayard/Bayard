package edu.usm.repository;

import edu.usm.domain.InteractionRecordType;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 3/17/16.
 */
public interface InteractionRecordTypeDao extends CrudRepository<InteractionRecordType, String>{

    @Override
    HashSet<InteractionRecordType> findAll();

}
