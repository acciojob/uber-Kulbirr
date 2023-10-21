package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class Customer{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;

    private String mobile;

    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<TripBooking> trips = new ArrayList<>();

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TripBooking> getTrips() {
        return trips;
    }

    public void setTrips(List<TripBooking> tips) {
        this.trips = trips;
    }

    public Customer(int customerId, String mobile, String password) {
        this.customerId = customerId;
        this.mobile = mobile;
        this.password = password;
    }

    public Customer(){

    }
}