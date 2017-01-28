/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning.impl;

import java.util.Iterator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;
import pl.arek.motoappserver.services.positioning.PersonalizedListBuilder;
import pl.arek.motoappserver.services.positioning.PersonalizedListManagerService;
import pl.arek.motoappserver.services.positioning.PersonalizedMemberList;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public final class PersonalizedListManagerServiceImpl implements PersonalizedListManagerService {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private GlobalMemberList globalMemberList;

    @Autowired
    private AccountService accountService;

    /**
     * store hashcode from globalList it will indicate when we have diferences
     * in gloal list
     */
    private int lastGlobalListHashCode;
    private double lastRadius;
    private PersonalizedMemberList cachedPersonalizedList;

    /**
     * set some initial values
     */
    public PersonalizedListManagerServiceImpl() {
        this.lastRadius = 0;
        this.lastGlobalListHashCode = 0;
        this.cachedPersonalizedList = new PersonalizedMemberList();
    }

    /**
     * we want to get brand new object for each time when we need, so we cant
     * just autowired it here because the parrent object is in session scope.
     *
     * @return
     */
    private PersonalizedListBuilder personalizedListBuilderFactory() {
        return this.beanFactory.getBean(PersonalizedListBuilder.class);
    }

    @Override
    public PersonalizedMemberList getPersonalizedList(double radius) {

        User currentUser = this.accountService.getCurrentUser();
        UserPosition currentUserPosition = getCurrentUserPosition(currentUser);
        PersonalizedListBuilder listBuilder = personalizedListBuilderFactory();

        PersonalizedMemberList list;
        if (isWereAnyChanges(radius)) {
            list = listBuilder.buildList(radius, currentUserPosition);
            this.cachedPersonalizedList = list; // rewiting our cache

        } else {
            list = this.cachedPersonalizedList;
        }

        return list;
    }

    private UserPosition getCurrentUserPosition(User currentUser) {
        Iterator globalListIterator = this.globalMemberList.getUsersPositions().iterator();
        UserPosition userPosition;
        while (globalListIterator.hasNext()) {
            userPosition = (UserPosition) globalListIterator.next();

            if (userPosition.getUser().equals(currentUser)) {
                return userPosition;
            }

        }
        return null;
    }

    private boolean isWereAnyChanges(double radius) {
        return isRadiusChanged(radius) || isGlobalListChanged() || isCacheEmpty();
    }

    private boolean isCacheEmpty() {
        return this.cachedPersonalizedList.getUsersPositions().isEmpty();
    }

    private boolean isGlobalListChanged() {
        return this.getGlobalListHashCode() != this.lastGlobalListHashCode;
    }

    private int getGlobalListHashCode() {
        int hash = globalMemberList.getUsersPositions().hashCode();
        return hash;
    }

    private boolean isRadiusChanged(double radius) {
        return radius != this.lastRadius;
    }
}
