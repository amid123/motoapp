/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.profile.impl;

import pl.arek.motoappserver.domain.entities.UserProfile;
import pl.arek.motoappserver.domain.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.services.profile.UserProfileService;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class UserProfileServiceImpl implements UserProfileService {
    
    @Autowired
    UserProfileRepository profileRepository;
    
    @Override
    public UserProfile getProfileByUserLogin(String login) {
        
        UserProfile profile = this.profileRepository.getProfileByUserName(login);

        /**
         * we need to breake recursive character of binding entities user and
         * user profile because when gson try to process it will die :).
         */
        profile.getUser().setUserProfile(null);
        profile.getPersonalSettings().setProfile(null);
        
        return profile;
    }
    
}
