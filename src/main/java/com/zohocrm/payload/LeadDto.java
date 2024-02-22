package com.zohocrm.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadDto {

    private String lid;
    private String firstName;
    private String lastName;
    private String email;
    private long mobile;
    private String leadType;
    private String address;
    private String designation;
    private String company;
    private String note;
}
