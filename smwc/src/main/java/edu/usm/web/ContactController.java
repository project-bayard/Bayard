package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Contact;
import edu.usm.domain.Views;
import edu.usm.mapper.ContactDtoMapper;
import edu.usm.mapper.ContactMapper;
import edu.usm.dto.ContactDto;
import edu.usm.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by scottkimball on 3/12/15.
 */

@RestController
@RequestMapping(value = "/contacts" )
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private ContactDtoMapper dtoMapper;

    private Logger logger = LoggerFactory.getLogger(ContactController.class);


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces={"application/json"})
    @JsonView(Views.ContactList.class)
    public Set<Contact> getContacts() {
        return contactService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"})
    public void createContact(@RequestBody ContactDto contactDto) {
        logger.debug("POST request to /contacts");
        Contact contact = contactMapper.convertDtoToContact(contactDto);
        contactService.create(contact);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/contact/{id}", produces={"application/json"})
    public ContactDto getContactById(@PathVariable("id") String id) {
        logger.debug("GET request to /contacts/contact/"+id);
        Contact contact = contactService.findById(id);
        return dtoMapper.convertToContactDto(contact);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/contact/{id}", consumes={"application/json"})
    public void updateContactById(@PathVariable("id") String id, @RequestBody ContactDto dto) {
        logger.debug("PUT request to /contacts/contact/"+id);
        Contact contact = contactMapper.convertDtoToContact(dto);
        contactService.update(contact);
    }

}

