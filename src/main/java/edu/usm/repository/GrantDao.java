package edu.usm.repository;

import edu.usm.domain.Grant;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by andrew on 1/24/16.
 */
public interface GrantDao extends CrudRepository<Grant, String>{

}
