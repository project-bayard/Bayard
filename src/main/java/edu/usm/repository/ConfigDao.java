package edu.usm.repository;

import edu.usm.domain.BayardConfig;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 4/25/16.
 */
public interface ConfigDao extends CrudRepository<BayardConfig, String> {

    @Override
    HashSet<BayardConfig> findAll();

}
