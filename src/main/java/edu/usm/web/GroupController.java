package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Aggregation;
import edu.usm.domain.Contact;
import edu.usm.domain.Group;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.GroupDto;
import edu.usm.dto.Response;
import edu.usm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by andrew on 10/10/15.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteGroup(@PathVariable("id") String id) {
        Group group = groupService.findById(id);
        groupService.delete(group);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response updateGroupName(@PathVariable("id") String id, @RequestBody GroupDto dto) throws ConstraintViolation{
        Group group = groupService.findById(id);
        group.setGroupName(dto.getGroupName());
        groupService.update(group);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createGroup(@RequestBody Group group) throws ConstraintViolation{
        String id = groupService.create(group);
        return new Response(id, Response.SUCCESS);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GroupList.class)
    public Set<Group> getGroups() {
        return groupService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GroupDetails.class)
    public Group getGroupById(@PathVariable("id") String id) {
        return groupService.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/all", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GroupDetails.class)
    public Set<Contact> getAllContactsInGroup(@PathVariable("id") String id) {
        Group g = groupService.findById(id);
        Set<Contact> allContacts = g.getTopLevelMembers();
        for (Aggregation aggregation: g.getAggregations()) {
            allContacts.addAll(aggregation.getAggregationMembers());
        }
        return allContacts;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/aggregations/{entityId}", consumes= {"application/json"}, produces = "application/json")
    public Response addAggregation(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws ConstraintViolation{
        Group group = groupService.findById(id);
        groupService.addAggregation(entityId, group);
        return new Response(id, Response.SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/aggregations/{entityId}")
    public Response removeAggregation(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws ConstraintViolation{
        Group group = groupService.findById(id);
        groupService.removeAggregation(entityId, group);
        return Response.successGeneric();
    }
}