/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@NamedQueries({
    @NamedQuery(name = "findUserByLogin", query = "from User u where u.login = :login")
    ,
    @NamedQuery(name = "findUserByEmail", query = "from User u where u.email = :email")
    ,
    @NamedQuery(name = "getAllUsers", query = "from User")
    ,
    @NamedQuery(name = "findUserByActivationHash", query = "from User where activationHash = :activationHash_param")
    ,
    @NamedQuery(name = "findUserByActivationHashExpired", query = "from User where registerDate < :register_date and activated = :activated")
    ,
    @NamedQuery(name = "updateUserPasswordByLogin", query = "UPDATE User u SET u.password = :password WHERE u.login = :login")
    ,
    @NamedQuery(name = "updateUserEmailByLogin", query = "UPDATE User u SET u.email = :email WHERE u.login = :login"),})
@Entity

@Table(name = "accounts", indexes = {
    @Index(name = "reg_date_index", columnList = "register_date", unique = false)})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String login;
    private String password;
    private String email;

    @NotNull
    @Type(type = "true_false")
    private Boolean activated;

    @NotNull
    @Column(name = "actvation_hash")
    private String activationHash;

    @NotNull
    @Column(name = "register_date")
    private Date registerDate;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserProfile userProfile;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public User() {
        this.activated = false;
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getActivationHash() {
        return activationHash;
    }

    public void setActivationHash(String activationHash) {
        this.activationHash = activationHash;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.login);
        hash = 29 * hash + Objects.hashCode(this.password);
        hash = 29 * hash + Objects.hashCode(this.email);
        hash = 29 * hash + Objects.hashCode(this.activated);
        hash = 29 * hash + Objects.hashCode(this.activationHash);
        hash = 29 * hash + Objects.hashCode(this.registerDate);
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
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.activationHash, other.activationHash)) {
            return false;
        }
        if (!Objects.equals(this.activated, other.activated)) {
            return false;
        }
        if (!Objects.equals(this.registerDate, other.registerDate)) {
            return false;
        }
        return true;
    }

}
