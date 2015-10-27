package edu.usm.repository;

import edu.usm.domain.EncounterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Created by scottkimball on 8/17/15.
 */

@Repository
public interface EncounterTypeDao extends CrudRepository<EncounterType, String> {

    EncounterType findByName(String name);
    @Override
    HashSet<EncounterType> findAll();
}
