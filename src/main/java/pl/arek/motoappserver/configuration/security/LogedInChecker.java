/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.security;

import pl.arek.motoappserver.domain.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */

public class LogedInChecker {

    public User getLoggedUser() {
        User user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            // sprawdzamy czy user nie jest anonimowy
            if (principal instanceof UserDetails) {
                AppUserDetails userDetails = (AppUserDetails) principal;
                user = userDetails.getUser();
            }
        }

        return user;
    }
}
