package com.zohocrm.service;

import com.zohocrm.payload.EmailDto;

public interface EmailService {
    public  EmailDto sendEmail(EmailDto emailDto);
}
