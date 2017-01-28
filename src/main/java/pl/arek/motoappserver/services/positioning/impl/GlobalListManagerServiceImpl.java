/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning.impl;

import pl.arek.motoappserver.services.positioning.GlobalListManagerService;
import com.google.gson.Gson;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.configuration.security.LogedInChecker;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class GlobalListManagerServiceImpl implements GlobalListManagerService {

    @Autowired
    LogedInChecker loggedInChecker;

    @Autowired
    GlobalMemberList globalMemberList;

    @Autowired
    Gson gson;

    private UserPosition userPosition;
    @Autowired
    SystemResponseFactory responseFactory;

    @Override
    public SystemResponse insertPositionToList(UserPosition position) {
        SystemResponse systemResponse;

        try {
            this.userPosition = position;

            if (isCurrentUserOnList()) {
                updateUserPositionOnList();

            } else {
                addNewUserPositionToList();
            }

            systemResponse = response(ResponseCode.SUCCESS);

        } catch (Exception ex) {
            systemResponse = response(ResponseCode.NOT_SUCCESS);
        }
        return systemResponse;
    }

    private SystemResponse response(ResponseCode code) {
        return this.responseFactory.createSystemResponse(code);
    }

    /**
     * First we need get iterator from global list; second iterate positions set
     * to find out where is currently logged user data; on the end when we find
     * user just update longitude and latidute values.
     */
    private void updateUserPositionOnList() {
        Iterator<UserPosition> i = this.globalMemberList.getUsersPositions().iterator();

        while (i.hasNext()) {
            UserPosition position = i.next();

            if (isUserEqualsToUser(position)) {
                position.setLatitude(this.userPosition.getLatitude());
                position.setLongitude(this.userPosition.getLongitude());
            }
        }
    }

    /**
     * Comparation of two users (User need to implement equals method without
     * two side associated relations.)
     *
     * @param position
     * @return
     */
    private boolean isUserEqualsToUser(UserPosition position) {
        return position.getUser().equals(this.userPosition.getUser());
    }

    /**
     * simple, just add new user to set because his not yet ther.
     */
    private void addNewUserPositionToList() {
        this.globalMemberList.getUsersPositions().add(this.userPosition);

        // if(this.globalMemberList.getUsersPositions().contains(this.userPosition))
    }

    /**
     * Chcecks if the user position is on list. it use equals method of
     * UserPositions with is not implemented by location values (longitude and
     * latitude). Just UserProfile. Also two ways associated fields in
     * UserProdile and User entities must to be excluded from these equals
     * implementations.
     *
     * @return boolean if found UserPosiotion associated with currently loged in
     * user.
     */
    private boolean isCurrentUserOnList() {
        return this.globalMemberList.getUsersPositions().contains(this.userPosition);
    }

}
