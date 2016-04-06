package edu.usm.repository;

import edu.usm.domain.SustainerPeriod;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Repository for sustainer periods.
 */
public interface SustainerPeriodDao extends CrudRepository<SustainerPeriod, String> {

    /**
     * Returns all existing sustainer periods
     * @return {@link java.util.Set} of {@link SustainerPeriod}
     */
    @Override
    HashSet<SustainerPeriod> findAll();

}
