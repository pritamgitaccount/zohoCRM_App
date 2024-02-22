package com.zohocrm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "emails")
public class Email {
    @Id
    private String eid;
    //here 'to' is a reserve keyword in MySQL that's why here written like this -->"`to`"
    // otherwise table creation will not happen
    @Column(name = "`to`")
    private String to;
    private String subject;
    private String message;
}
