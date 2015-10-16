package edu.usm.service;

import edu.usm.domain.Aggregation;
import edu.usm.domain.Group;

import java.util.Set;

/**
 * Created by andrew on 10/8/15.
 */
public interface GroupService {

    String create(Group group);
    void update(Group group);
    void delete(Group group);
    Group findById(String id);
    Set<Group> findAll();
    void deleteAll();
    void addAggregation(String aggregationId, Group group);
    void addAggregation(Aggregation aggregation, Group group);
    void removeAggregation(String aggregationId, Group group);
    void removeAggregation(Aggregation aggregation, Group group);

}
