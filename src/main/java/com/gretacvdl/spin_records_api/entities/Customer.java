package com.gretacvdl.spin_records_api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

    private Long id;
    private String email;
    private String name;
    private Timestamp createdAt;
}