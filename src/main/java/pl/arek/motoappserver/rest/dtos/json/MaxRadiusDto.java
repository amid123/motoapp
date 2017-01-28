/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.rest.dtos.json;

import pl.arek.motoappserver.rest.dtos.*;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class MaxRadiusDto {
    
    
     private double radius;

        public MaxRadiusDto() {
            this.radius = 5000;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }
}
