package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Contact;
import edu.usm.domain.Views;
import edu.usm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by scottkimball on 3/12/15.
 */

@RestController
@RequestMapping(value = "/contacts" )
public class ContactController {

    @Autowired
    private ContactService contactService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces={"application/json"})
    @JsonView(Views.ContactList.class)
    public List<Contact> getContacts() {
        return contactService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"})
    public void createContact(@RequestBody Contact contact) {

        contactService.create(contact);

    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET,value = "/contact/{id}", produces={"application/json"})
    @JsonView(Views.ContactDetails.class)
    public Contact getContactById(@PathVariable("id") String id) {
        return contactService.findById(id);
    }



}

