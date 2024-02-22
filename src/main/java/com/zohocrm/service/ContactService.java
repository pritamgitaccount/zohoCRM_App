package com.zohocrm.service;

import com.zohocrm.payload.ContactDto;

public interface ContactService {
    ContactDto createContact(String leadId);
}
