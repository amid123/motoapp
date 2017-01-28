/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories;

import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.domain.entities.UserProfile;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface UserProfileRepository {

    public UserProfile getProfileByUserName(String userName);

    public boolean addNewProfileProfile(UserProfile profile);
    public boolean isUserProfileExist(String login);
//    public boolean deleteUserProfile(User user);

}
