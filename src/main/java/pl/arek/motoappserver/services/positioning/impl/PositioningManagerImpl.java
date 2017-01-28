/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning.impl;

import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;
import pl.arek.motoappserver.services.positioning.PersonalizedMemberList;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class PositioningManagerImpl {

    @Autowired
    GlobalMemberList globalMemberList;

    PersonalizedMemberList personalizedMemberList;

    public PositioningManagerImpl() {
        this.personalizedMemberList = new PersonalizedMemberList();
    }

    public void updatePositions() {

    }

    class UpdatePosition implements Callable<UserPosition> {

        double lat1, lat2, lon1, lon2;
        String sr;
        int distance;

        public UpdatePosition(double lat1, double lat2, double lon1, double lon2, int distance) {
            
            this.lat1 = lat1;
            this.lat2 = lat2;
            this.lon1 = lon1;
            this.lon2 = lon2;
            this.sr = "M";
        }

        public UpdatePosition(double lat1, double lat2, double lon1, double lon2, int distance, String sr) {
            this.lat1 = lat1;
            this.lat2 = lat2;
            this.lon1 = lon1;
            this.lon2 = lon2;
            this.sr = sr;
        }

        @Override
        public UserPosition call() throws Exception {

           // if(distance()<this.distance)
               return null; 
        }

        public double distance() {

            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (sr.equals("K")) {
                dist = dist * 1.609344;
            } else if (sr.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        public double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        public double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }
    }
}
