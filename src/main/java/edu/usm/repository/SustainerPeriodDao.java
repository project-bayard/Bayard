package edu.usm.repository;

import edu.usm.domain.SustainerPeriod;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 1/23/16.
 */
public interface SustainerPeriodDao extends CrudRepository<SustainerPeriod, String> {

    @Override
    HashSet<SustainerPeriod> findAll();

}
