package com.zohocrm.exception;

public class ContactExist extends RuntimeException {
    public ContactExist(String message) {
        super(message);
    }
}
