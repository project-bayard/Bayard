package edu.usm.service;

import edu.usm.domain.Committee;

import java.util.Set;

/**
 * Created by scottkimball on 4/15/15.
 */
public interface CommitteeService {

    Committee findById (String id);
    Set<Committee> findAll();
    void delete (Committee committee);
    void update (Committee committee);
    String create (Committee committee);
    void deleteAll();
}
