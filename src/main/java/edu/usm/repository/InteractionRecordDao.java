package edu.usm.repository;

import edu.usm.domain.InteractionRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 1/24/16.
 */
public interface InteractionRecordDao extends CrudRepository<InteractionRecord, String>{

    @Override
    HashSet<InteractionRecord> findAll();

}
