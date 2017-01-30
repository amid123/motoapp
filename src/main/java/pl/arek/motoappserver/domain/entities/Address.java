/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String city;
    private String country;
    private String region;
    private String street;
    private String postCode;
    private int homeNo;
    private int flatNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public int getHomeNo() {
        return homeNo;
    }

    public void setHomeNo(int homeNo) {
        this.homeNo = homeNo;
    }

    public int getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(int flatNo) {
        this.flatNo = flatNo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.city);
        hash = 29 * hash + Objects.hashCode(this.country);
        hash = 29 * hash + Objects.hashCode(this.region);
        hash = 29 * hash + Objects.hashCode(this.street);
        hash = 29 * hash + Objects.hashCode(this.postCode);
        hash = 29 * hash + this.homeNo;
        hash = 29 * hash + this.flatNo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.homeNo != other.homeNo) {
            return false;
        }
        if (this.flatNo != other.flatNo) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.region, other.region)) {
            return false;
        }
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        if (!Objects.equals(this.postCode, other.postCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", city=" + city + ", country=" + country + ", region=" + region + ", street=" + street + ", postCode=" + postCode + ", homeNo=" + homeNo + ", flatNo=" + flatNo + '}';
    }

}
