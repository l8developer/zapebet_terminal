package com.bet.mpos.objects;

import androidx.annotation.NonNull;

public class BetCustomerRegistration {
    private String register_name, document_value, email, uuid;
    private Long phone_number;

    public BetCustomerRegistration(String register_name, String document_value, String email, String uuid, Long phone_number) {
        this.register_name = register_name;
        this.document_value = document_value;
        this.email = email;
        this.uuid = uuid;
        this.phone_number = phone_number;
    }

    public String getRegister_name() {
        return register_name;
    }

    public void setRegister_name(String register_name) {
        this.register_name = register_name;
    }

    public String getDocument_value() {
        return document_value;
    }

    public void setDocument_value(String document_value) {
        this.document_value = document_value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Long phone_number) {
        this.phone_number = phone_number;
    }

    @NonNull
    @Override
    public String toString() {
        return "{ " +
                "register_name: " + register_name + ", " +
                "document_value: " + document_value + ", " +
                "phone_number: " + phone_number + ", " +
                "email: " + email + ", " +
                "uuid: " + uuid +
                " }";
    }
}
