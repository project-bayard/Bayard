package edu.usm.repository;

import edu.usm.domain.InteractionRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Repository for Interaction Records
 */
public interface InteractionRecordDao extends CrudRepository<InteractionRecord, String>{

    /**
     * Returns all existing interation records.
     * @return {@link java.util.Set} of {@link InteractionRecord}
     */
    @Override
    HashSet<InteractionRecord> findAll();

}
