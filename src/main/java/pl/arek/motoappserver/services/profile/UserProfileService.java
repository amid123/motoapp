/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.profile;

import pl.arek.motoappserver.domain.entities.UserProfile;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface UserProfileService {
    
    UserProfile getProfileByUserLogin(String login);
    
}
