/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.rest.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.rest.dtos.json.MaxRadiusDto;
import pl.arek.motoappserver.services.positioning.PersonalizedListManagerService;
import pl.arek.motoappserver.services.positioning.PersonalizedMemberList;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RestController
@RequestMapping("/members")
public class MembersListController {

    private static final Logger logger = Logger.getLogger(MembersListController.class.getName());

    @Autowired
    private SystemResponseFactory systemResponseFactory;

    @Autowired
    private PersonalizedListManagerService personalizedListManager;

    @Autowired
    private Gson gson;

    @RequestMapping(
            value = "list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public SystemResponse getPersonalizedMembersList(HttpServletRequest request) {

        double radius;
        radius = getRadiusFromRequest(request);

        /**
         * We just trying here to recive personalized list and return it as
         * attachment field in SystemResponse. If for some reason list can't be
         * build we will recive null as attachment.
         */
        SystemResponse response;
        PersonalizedMemberList memberList = personalizedListManager.getPersonalizedList(radius);
        
        if (memberList == null) {
            response = response(ResponseCode.NOT_SUCCESS);
        } else {
            response = response(ResponseCode.SUCCESS);
        }

        return response;
    }

    private double getRadiusFromRequest(HttpServletRequest request) throws JsonSyntaxException, JsonIOException {
        MaxRadiusDto radius = new MaxRadiusDto();
        try {
            radius = gson.fromJson(request.getReader(), MaxRadiusDto.class);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return radius.getRadius();
    }

    private SystemResponse response(ResponseCode code) {
        return this.systemResponseFactory.createSystemResponse(code);
    }
}
