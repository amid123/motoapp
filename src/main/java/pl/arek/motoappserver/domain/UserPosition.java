/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain;

import java.util.Objects;
import javax.persistence.Transient;
import pl.arek.motoappserver.domain.entities.User;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public final class UserPosition extends Position {

    User user;

    public UserPosition() {

        this.longitude = 0;
        this.latitude = 0;
    }

    public UserPosition(User user, double latitude, double longitude) {
        super.latitude = latitude;
        super.longitude = longitude;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * We need hashCode here only for user to associate position only by user
     * not by position in map.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.user);
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
        final UserPosition other = (UserPosition) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

}
