package edu.usm.it.service;

import edu.usm.domain.Committee;

import java.util.List;

/**
 * Created by scottkimball on 4/15/15.
 */
public interface CommitteeService {

    Committee findById (String id);
    List<Committee> findAll();
    void delete (Committee committee);
    void update (Committee committee);
    void create (Committee committee);
    void deleteAll();
}
