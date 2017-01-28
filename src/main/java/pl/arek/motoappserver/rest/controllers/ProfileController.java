/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.rest.controllers;

import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.domain.entities.UserProfile;
import pl.arek.motoappserver.services.profile.UserProfileService;
import java.io.Serializable;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RestController
@Scope("request")
@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController implements Serializable {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProfileController.class.getName());
    
    @Autowired
    UserProfileService profileService;
    
    @Autowired
    AccountService accountService;
    
    @RequestMapping(method = GET)
    ResponseEntity<UserProfile> getCurrentUserProfile() {
        
        String userLigin = this.accountService.getCurrentUser().getLogin();
        UserProfile profile = this.profileService.getProfileByUserLogin(userLigin);
       
        
 
        return ok(profile);
    }
    
}
