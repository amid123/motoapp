/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning.impl;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.arek.motoappserver.services.positioning.PersonalizedListBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;
import pl.arek.motoappserver.services.positioning.PersonalizedMemberList;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public final class PersonalizedListBuilderImpl implements PersonalizedListBuilder {

    private static final Logger logger = Logger.getLogger(PersonalizedListBuilderImpl.class.getName());
    /**
     * We dont want service here because there is no any bussines logic only
     * simple access to data stored in db; also this repository has only access
     * to update each options and get all options. There is no need to
     * create/delete options here.
     */
    private double maxRadiusDistance;
    private UserPosition currentClientPosition;

    /**
     * Constructor consumes radius for building members list and current user
     * position
     *
     * @param maxRadiusDistance
     * @param currentUserPosition
     */
//    public PersonalizedListBuilderImpl(double maxRadiusDistance, UserPosition currentUserPosition) {
//        this.maxRadiusDistance = maxRadiusDistance;
//        this.currentClientPosition = currentUserPosition;
//    }
    @Autowired
    private GlobalMemberList globalMemberList;

    /**
     * Building new personalized members list first we created thread and task
     * here execute it in single executor and w8 for results.
     *
     * @return
     */
    @Override
    public PersonalizedMemberList buildList(double maxRadiusDistance, UserPosition currentUserPosition) {
        this.maxRadiusDistance = maxRadiusDistance;
        this.currentClientPosition = currentUserPosition;

        FutureTask<PersonalizedMemberList> task = createTask();
        ExecutorService singleExecutor = createTaskExecutor();
        singleExecutor.execute(task);
        PersonalizedMemberList memberList = waitForResults(task);
        return memberList;
    }

    /**
     * waiting for results here if there was some exception log it and return
     * null
     *
     * @param task
     * @return
     */
    private PersonalizedMemberList waitForResults(FutureTask<PersonalizedMemberList> task) {

        while (!task.isDone());
        PersonalizedMemberList memberList = null;
        try {
            memberList = task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return memberList;
    }

    /**
     * creating single executor
     *
     * @return
     */
    private ExecutorService createTaskExecutor() {
        ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
        return singleExecutor;
    }

    /**
     * building task form callable thread
     *
     * @return
     */
    private FutureTask<PersonalizedMemberList> createTask() {
        ListBuilderThread thread = new ListBuilderThread(this.currentClientPosition, this.maxRadiusDistance);
        FutureTask<PersonalizedMemberList> task = new FutureTask<>(thread);
        return task;
    }

    /**
     * *
     * here we have listBuilder Thread
     */
    private final class ListBuilderThread implements Callable<PersonalizedMemberList> {

        UserPosition clientPositionBuffer;
        double maxRadius;

        public ListBuilderThread(UserPosition clientCurrentPosition, double maxRadius) {
            this.clientPositionBuffer = clientCurrentPosition;
            this.maxRadius = maxRadius;
        }

        @Override
        public PersonalizedMemberList call() throws Exception {

            Iterator listIterator = getGlobalListIterator();
            PersonalizedMemberList memberList = new PersonalizedMemberList();

            double distance;

            UserPosition position;

            // iteratin on global members list
            while (listIterator.hasNext()) {
                position = getNext(listIterator);

                /**
                 * if this is is current user just breake this iteration
                 * and get next that is because we need to exclue ourselves from
                 * the list
                 */
                if (isItCurrentUser(position)) {
                    continue;
                }

                distance = calculateDistanceBetwenCurrentUserAnd(position);

                /**
                 * if distance is less then max ratius this is our member. and
                 * we need him on the list
                 */
                if (distance < this.maxRadius) {
                    addUserPositionToList(memberList, position);
                }
            }
            return memberList;
        }

        /**
         * checks if the given user in UserPosition is same as currentUser in
         * our object.
         *
         * @param position
         * @return
         */
        private boolean isItCurrentUser(UserPosition position) {
            return position.getUser().equals(this.clientPositionBuffer.getUser());
        }

        /**
         * calculating distance betwen given UserPosition and current
         * UserPostion with is in our obiect;
         *
         * @param position
         * @return
         */
        private double calculateDistanceBetwenCurrentUserAnd(UserPosition position) {
            double distance;
            // calculations of distance betwene current user and
            // iterated user form global list
            distance = distance(
                    this.clientPositionBuffer.getLatitude(),
                    this.clientPositionBuffer.getLongitude(),
                    position.getLatitude(),
                    position.getLongitude());
            return distance;
        }

        private void addUserPositionToList(PersonalizedMemberList memberList1, UserPosition position) {
            memberList1.getUsersPositions().add(position);
        }

        private UserPosition getNext(Iterator listIterator) {
            UserPosition position;
            position = (UserPosition) listIterator.next();
            return position;
        }

        private Iterator getGlobalListIterator() {
            Iterator listIterator = globalMemberList.getUsersPositions().iterator();
            return listIterator;
        }

        /**
         * calculate distance betwent two points. This code is from
         * stackoverflow.
         *
         * @param lat1
         * @param lat2
         * @param lon1
         * @param lon2
         * @return
         */
        private double distance(double lat1, double lon1, double lat2,
                double lon2) {

            final int R = 6371; // Radius of the earth

            Double latDistance = Math.toRadians(lat2 - lat1);
            Double lonDistance = Math.toRadians(lon2 - lon1);
            Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convert to meters

           // double height = 0.0;

            distance = Math.pow(distance, 2);// + Math.pow(height, 2);

            return Math.sqrt(distance);
        }

    }

}
