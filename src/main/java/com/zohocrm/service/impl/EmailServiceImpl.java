package com.zohocrm.service.impl;

import com.zohocrm.entity.Contact;
import com.zohocrm.entity.Email;
import com.zohocrm.entity.Lead;
import com.zohocrm.exception.ContactExist;
import com.zohocrm.exception.LeadNotFoundException;
import com.zohocrm.payload.EmailDto;
import com.zohocrm.repository.ContactRepository;
import com.zohocrm.repository.EmailRepository;
import com.zohocrm.repository.LeadRepository;
import com.zohocrm.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final LeadRepository leadRepository;
    private final ModelMapper modelMapper;
    private final ContactRepository contactRepository;

    public EmailServiceImpl(
            JavaMailSender javaMailSender,
            EmailRepository emailRepository,
            LeadRepository leadRepository,
            ModelMapper modelMapper,
            ContactRepository contactRepository) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
        this.leadRepository = leadRepository;
        this.modelMapper = modelMapper;
        this.contactRepository = contactRepository;
    }

    @Override
    public EmailDto sendEmail(EmailDto emailDto) {

        Lead lead = leadRepository.findByEmail(emailDto.getTo()).orElseThrow(
                () -> new LeadNotFoundException("Email Id not registered" + emailDto.getTo()));

//        Contact contact = contactRepository.findByEmail(emailDto.getTo()).orElseThrow(
//                () -> new ContactExist("Email Id not registered" + emailDto.getTo()));


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getMessage());
        javaMailSender.send(message);

        Email email = mapToEntity(emailDto);
        email.setEid(UUID.randomUUID().toString());
        return mapToDto(emailRepository.save(email));
    }

    Email mapToEntity(EmailDto emailDto) {
        return modelMapper.map(emailDto, Email.class);
    }

    EmailDto mapToDto(Email email) {
        return modelMapper.map(email, EmailDto.class);
    }
}
