package com.zohocrm.service.impl;

import com.zohocrm.entity.Contact;
import com.zohocrm.entity.Lead;
import com.zohocrm.exception.LeadNotFoundException;
import com.zohocrm.payload.ContactDto;
import com.zohocrm.repository.ContactRepository;
import com.zohocrm.repository.LeadRepository;
import com.zohocrm.service.ContactService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ContactDto createContact(String leadId) {

        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new LeadNotFoundException("Lead with Id " + leadId + " does not exist."));


        Contact contact = createLeadToContact(lead);
        String cid = UUID.randomUUID().toString();
        contact.setCid(cid);
        Contact savedContact = contactRepository.save(contact);

        if (savedContact.getCid() != null) {
            leadRepository.deleteById(lead.getLid());
        }

        ContactDto dto = mapToDto(savedContact);
        dto.setCid(savedContact.getCid());
        return dto;
    }

    private ContactDto mapToDto(Contact contact) {
        return modelMapper.map(contact, ContactDto.class);
    }

    private Contact createLeadToContact(Lead lead) {
        return modelMapper.map(lead, Contact.class);
    }
}
