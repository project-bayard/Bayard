package edu.usm.repository;

import edu.usm.domain.Grant;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 1/24/16.
 */
public interface GrantDao extends CrudRepository<Grant, String>{

    @Override
    HashSet<Grant> findAll();

}
