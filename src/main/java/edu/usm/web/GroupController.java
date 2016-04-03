package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Contact;
import edu.usm.domain.Group;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
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
    public Response deleteGroup(@PathVariable("id") String id) throws NullDomainReference, ConstraintViolation {
        groupService.delete(id);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response updateGroupName(@PathVariable("id") String id, @RequestBody GroupDto dto)
            throws ConstraintViolation, NullDomainReference.NullGroup{
        groupService.updateDetails(id,dto);
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
    public Group getGroupById(@PathVariable("id") String id) throws NullDomainReference.NullGroup {
        return groupService.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/all", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GroupDetails.class)
    public Set<Contact> getAllContactsInGroup(@PathVariable("id") String id) {
        return  groupService.getAllMembers(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{groupId}/aggregations/{aggId}", consumes= {"application/json"}, produces = "application/json")
    public Response addAggregation(@PathVariable("groupId") String groupId, @PathVariable("aggId") String aggId)
            throws ConstraintViolation, NullDomainReference.NullAggregation, NullDomainReference.NullGroup{
        groupService.addAggregation(aggId, groupId);
        return new Response(groupId, Response.SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{groupId}/aggregations/{aggId}")
    public Response removeAggregation(@PathVariable("groupId") String groupId, @PathVariable("aggId") String aggId)
            throws ConstraintViolation, NullDomainReference.NullGroup, NullDomainReference.NullAggregation{
        groupService.removeAggregation(aggId, groupId);
        return Response.successGeneric();
    }
}