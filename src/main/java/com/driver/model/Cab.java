package com.driver.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Driver;

@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class Cab{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int perKmRate;

    private boolean available;

    @OneToOne
    @JoinColumn
    Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Cab(int id, int perKmRate, boolean available) {
        this.id = id;
        this.perKmRate = perKmRate;
        this.available = available;
    }

    public Cab() {
    }


}