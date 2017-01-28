/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@NamedQueries({
    @NamedQuery(name = "getSettingsByUserName", query = "SELECT s FROM PersonalSettings s WHERE s.profile.user.login = :login"),
    @NamedQuery(name = "updateMembersCount", query = "UPDATE PersonalSettings AS S SET S.membersCount =:members_count WHERE  S.profile.user.login = :login")
})
@Entity
@Table(name = "settings")
public class PersonalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToOne(mappedBy = "personalSettings", fetch = FetchType.LAZY)
    UserProfile profile;

    /**
     * max members on personalized list option.
     */
    int membersCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 83 * hash + this.membersCount;
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
        final PersonalSettings other = (PersonalSettings) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.membersCount != other.membersCount) {
            return false;
        }
        return true;
    }
}
