package edu.usm.repository;

import edu.usm.domain.Encounter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface EncounterDao extends CrudRepository<Encounter, String> {

    @Override
    List<Encounter> findAll();
}
