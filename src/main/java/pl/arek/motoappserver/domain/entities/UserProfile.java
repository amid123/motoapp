/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@NamedQueries({
    @NamedQuery(name = "getProfileByUserName", query = "select p from UserProfile p where p.user.login = :login")
    ,
    @NamedQuery(name = "dropUserProfileByUserName", query = "delete from UserProfile p where p.user.login = :login")})
@Table(name = "profile")
@Entity
public class UserProfile implements Serializable {

    public enum HowFast {
        SLOW("SLOW"), NORMAL("NORMAL"), FAST("FAST"), HARD("HARD");
        String value;

        private HowFast(String v) {
            value = v;
        }

        @Override
        public String toString() {
            return value;
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "settings_id")
    private PersonalSettings personalSettings;

    @OneToOne(optional = true, orphanRemoval = true)
    @JoinColumn(name = "personal_details_id")
    private PersonalDetails personalDetails;

    //private String firstName;
    private byte[] photo;
    private HowFast howFast;
    private String bikeType;
    private String userCity;
    private String decrtiption;
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PersonalSettings getPersonalSettings() {
        return personalSettings;
    }

    public void setPersonalSettings(PersonalSettings personalSettings) {
        this.personalSettings = personalSettings;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public HowFast getHowFast() {
        return howFast;
    }

    public void setHowFast(HowFast howFast) {
        this.howFast = howFast;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getDecrtiption() {
        return decrtiption;
    }

    public void setDecrtiption(String decrtiption) {
        this.decrtiption = decrtiption;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + Arrays.hashCode(this.photo);
        hash = 53 * hash + Objects.hashCode(this.howFast);
        hash = 53 * hash + Objects.hashCode(this.bikeType);
        hash = 53 * hash + Objects.hashCode(this.userCity);
        hash = 53 * hash + Objects.hashCode(this.decrtiption);
        hash = 53 * hash + this.age;
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
        final UserProfile other = (UserProfile) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.age != other.age) {
            return false;
        }
        if (!Objects.equals(this.bikeType, other.bikeType)) {
            return false;
        }
        if (!Objects.equals(this.userCity, other.userCity)) {
            return false;
        }
        if (!Objects.equals(this.decrtiption, other.decrtiption)) {
            return false;
        }
        if (!Arrays.equals(this.photo, other.photo)) {
            return false;
        }
        if (this.howFast != other.howFast) {
            return false;
        }
        return true;
    }

}
