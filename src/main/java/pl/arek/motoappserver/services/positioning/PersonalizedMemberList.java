/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning;

import java.util.HashSet;
import java.util.Set;

import pl.arek.motoappserver.domain.UserPosition;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class PersonalizedMemberList {

    Set<UserPosition> usersPositions;

    public PersonalizedMemberList() {
        this.usersPositions = new HashSet();
    }

    public Set<UserPosition> getUsersPositions() {
        return usersPositions;
    }
}
