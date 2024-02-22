package com.zohocrm.controller;

import com.zohocrm.exception.LeadNotFoundException;
import com.zohocrm.payload.ContactDto;
import com.zohocrm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

//    public ContactController(ContactService contactService) {
//        this.contactService = contactService;
//    }

// http://localhost:8080/api/contacts/{leadId}

    @PostMapping("/{leadId}")
    public ResponseEntity<?> createContact(@PathVariable String leadId) {
        if (!leadId.isEmpty()) {
            try {
                ContactDto dto = contactService.createContact(leadId);
                return new ResponseEntity<>(dto, HttpStatus.CREATED);
            } catch (LeadNotFoundException e) {
                return new ResponseEntity<>("Lead with Id " + leadId + " does not exist.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Lead id is not present :" + leadId, HttpStatus.BAD_REQUEST);
        }
    }
}

