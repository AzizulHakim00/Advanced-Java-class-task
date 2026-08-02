package com.sparktech.shop.student;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(name = "street_address")
    private String streetAddress;
    private String city;
    private String state;
    private String country;
}
