package edu.usm.service.impl;

import edu.usm.domain.Aggregation;
import edu.usm.domain.Contact;
import edu.usm.domain.Group;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.repository.AggregationDao;
import edu.usm.repository.GroupDao;
import edu.usm.service.ContactService;
import edu.usm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrew on 10/8/15.
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ContactService contactService;

    @Autowired
    private AggregationDao aggregationDao;

    @Override
    public String create(Group group) throws ConstraintViolation{
        validateUniqueness(group);
        groupDao.save(group);
        return group.getId();
    }

    @Override
    public Group findByName(String groupName) {
        return groupDao.findByGroupName(groupName);
    }

    private void validateUniqueness(Group group) throws ConstraintViolation {

        if (null == group.getGroupName()) {
            throw new ConstraintViolation(ConstraintMessage.GROUP_REQUIRED_NAME);
        }

        Group existingGroup = findByName(group.getGroupName());
        if (null != existingGroup && !existingGroup.getId().equalsIgnoreCase(group.getId())) {
            throw new ConstraintViolation(ConstraintMessage.GROUP_NON_UNIQUE);
        }
    }

    @Override
    public void update(Group group) throws ConstraintViolation {
        validateUniqueness(group);
        groupDao.save(group);
    }

    @Override
    public void delete(Group group) {

        if (null != group.getAggregations()) {
            for (Aggregation aggregation: group.getAggregations()) {
                aggregation.getGroups().remove(group);
                aggregationDao.save(aggregation);
            }
        }

        if (null != group.getTopLevelMembers()) {
            Set<Contact> contacts = new HashSet<>(group.getTopLevelMembers());
            for (Contact contact: contacts) {
                contactService.removeFromGroup(contact, group);
            }
        }
        groupDao.delete(group);
    }

    @Override
    public Set<Group> findAll() {
        return groupDao.findAll();
    }

    @Override
    public void deleteAll() {
        Set<Group> groups = findAll();
        groups.stream().forEach(this::delete);
    }

    @Override
    public Group findById(String id) {
        return groupDao.findOne(id);
    }

    @Override
    public void addAggregation(String aggregationId, Group group) throws ConstraintViolation{
        Aggregation aggregation = aggregationDao.findOne(aggregationId);
        addAggregation(aggregation, group);
    }

    @Override
    public void addAggregation(Aggregation aggregation, Group group) throws ConstraintViolation{
        aggregation.getGroups().add(group);
        group.getAggregations().add(aggregation);
        update(group);
    }

    @Override
    public void removeAggregation(String aggregationId, Group group) throws ConstraintViolation{
        Aggregation aggregation = aggregationDao.findOne(aggregationId);
        removeAggregation(aggregation, group);
    }

    @Override
    public void removeAggregation(Aggregation aggregation, Group group) throws ConstraintViolation{
        Set<Group> groups = aggregation.getGroups();
        Set<Aggregation> aggregations = group.getAggregations();
        groups.remove(group);
        aggregations.remove(aggregation);
        update(group);
    }
}
