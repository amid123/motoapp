/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.rest.controllers;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.services.positioning.GlobalListManagerService;
import pl.arek.motoappserver.domain.SystemResponse;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RestController
@Scope("session")
@RequestMapping(value = "/myposition")
public class MyPosiotionController {
    
    @Autowired
    GlobalListManagerService globalListManagerService;
    
    @Autowired
    Gson gson;
    
    @Autowired
    AccountService accountService;

    /**
     * This is mapping method for save current user location to global list
     *
     * @param latitude
     * @param longitude
     * @return
     */
    @RequestMapping(
            value = "/save",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    
    public ResponseEntity<SystemResponse> saveMyPosition(HttpServletRequest request) {
        SystemResponse systemResponse = new SystemResponse();
        try {
            
            UserPosition position = gson.fromJson(request.getReader().readLine(), UserPosition.class);
            
            User user = this.accountService.getCurrentUser();
            
            position.setUser(user);
            
            systemResponse = globalListManagerService.insertPositionToList(position);
            //   systemResponse = insertPositionToList(position);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ok(systemResponse);
    }
}
