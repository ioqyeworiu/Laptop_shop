package com.lapstore.LaptopShop.model;

import lombok.Data;

@Data
public class OrderRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNo;

    private String address;

    private String city;

    private String pincode;

    private String paymentMethod;
}
