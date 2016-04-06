package edu.usm.service.impl;

import edu.usm.domain.Aggregation;
import edu.usm.domain.Contact;
import edu.usm.domain.Group;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.GroupDto;
import edu.usm.repository.AggregationDao;
import edu.usm.repository.GroupDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import edu.usm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrew on 10/8/15.
 */
@Service
public class GroupServiceImpl extends BasicService implements GroupService {

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

    private Group findByName(String groupName)  {
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
    public void update(Group group) throws ConstraintViolation, NullDomainReference.NullGroup {
        validateUniqueness(group);
        groupDao.save(group);
    }

    @Override
    public void updateDetails(String id, GroupDto groupDto) throws ConstraintViolation, NullDomainReference.NullGroup {
        Group group = findGroup(id);
        group.setGroupName(groupDto.getGroupName());
        validateUniqueness(group);
        groupDao.save(group);
    }

    @Override
    @Transactional
    public void delete(String id) throws NullDomainReference, ConstraintViolation  {
        Group group = findGroup(id);
        if (null != group.getAggregations()) {
            Set<Aggregation> aggregations = new HashSet<>(group.getAggregations());
            for (Aggregation aggregation: aggregations) {
                aggregation.getGroups().remove(group);
            }
        }

        if (null != group.getTopLevelMembers()) {
            Set<Contact> contacts = new HashSet<>(group.getTopLevelMembers());
            for (Contact contact: contacts) {
                contactService.removeFromGroup(contact.getId(), group.getId());
            }
        }
        groupDao.delete(group);
    }

    @Override
    public Set<Group> findAll() {
        return groupDao.findAll();
    }

    @Override
    @Transactional
    public void deleteAll()  {
        findAll().stream().forEach(this::uncheckedDelete);
    }

    @Override
    @Transactional
    public Group findById(String id) throws NullDomainReference.NullGroup {
        Group group = findGroup(id);
        initializeCollections(group);
        return group;
    }

    @Override
    @Transactional
    public void addAggregation(String aggregationId, String groupId) throws ConstraintViolation, NullDomainReference.NullGroup{
        Group group = findGroup(groupId);
        Aggregation aggregation = aggregationDao.findOne(aggregationId);
        addAggregation(aggregation, group);
    }


    @Override
    @Transactional
    public void removeAggregation(String aggregationId, String groupId) throws ConstraintViolation{
        Group group = groupDao.findOne(groupId);
        Aggregation aggregation = aggregationDao.findOne(aggregationId);
        removeAggregation(aggregation, group);
    }

    @Override
    @Transactional
    public Set<Contact> getAllMembers(String id) {
        Group group = groupDao.findOne(id);
        Set<Contact> allContacts = group.getTopLevelMembers();
        for (Aggregation aggregation: group.getAggregations()) {
            allContacts.addAll(aggregation.getAggregationMembers());
        }
        return allContacts;
    }

    private void initializeCollections(Group group) {
        Set<Aggregation> aggregations = group.getAggregations();
        Set<Contact> members = group.getTopLevelMembers();

        if (aggregations == null) {
            aggregations = new HashSet<>();
        }

        if (members == null) {
            members = new HashSet<>();
        }

        aggregations.size();
        members.size();
    }

    private void addAggregation(Aggregation aggregation, Group group) throws ConstraintViolation{
        aggregation.getGroups().add(group);
        group.getAggregations().add(aggregation);
        groupDao.save(group);
    }

    private void removeAggregation(Aggregation aggregation, Group group) throws ConstraintViolation{
        Set<Group> groups = aggregation.getGroups();
        Set<Aggregation> aggregations = group.getAggregations();
        groups.remove(group);
        aggregations.remove(aggregation);
        groupDao.save(group);
    }

    private Group findGroup(String groupId) throws NullDomainReference.NullGroup {
        Group group = groupDao.findOne(groupId);
        if (group == null) {
            throw new NullDomainReference.NullGroup(groupId);
        }
        return group;
    }

    private Aggregation findAggregation(String aggId) throws NullDomainReference.NullAggregation {
        Aggregation aggregation = aggregationDao.findOne(aggId);

        if (aggregation == null) {
            throw new NullDomainReference.NullAggregation(aggId);
        }
        return aggregation;
    }


}
