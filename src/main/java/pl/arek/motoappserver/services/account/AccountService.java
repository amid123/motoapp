/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.account;

import pl.arek.motoappserver.domain.entities.User;
import java.util.List;
import pl.arek.motoappserver.domain.SystemResponse;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface AccountService {

    User getUserByUsername(String username);

    List<String> getPermissions(String username);

    User getCurrentUser();

    SystemResponse registerNewAccount(User user);

    boolean isUserRegistered(String username);

    boolean isCurrentUserLoggedIn();

    SystemResponse dropUser(User u);

    SystemResponse updateUser(String username, User u);

    SystemResponse changeUserPassword(User user, String oldPassword, String newPassword);

    SystemResponse changeUserEmail(User user, String newEmail);

}
