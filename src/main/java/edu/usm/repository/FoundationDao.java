package edu.usm.repository;

import edu.usm.domain.Foundation;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 1/24/16.
 */
public interface FoundationDao extends CrudRepository<Foundation, String> {

    @Override
    HashSet<Foundation> findAll();

    Foundation findByName(String name);

}
