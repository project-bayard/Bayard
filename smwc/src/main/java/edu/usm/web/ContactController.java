package edu.usm.web;

import edu.usm.domain.Contact;
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
    public List<Contact> getContacts() {
        return contactService.findAll();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET,value = "/contact/{id}", produces={"application/json"})
    public Contact getContactById(@PathVariable("id") long id) {

        Contact contact = contactService.findById(id);
        return contact;
    }



}

