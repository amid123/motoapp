/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.security;

import com.google.gson.Gson;
import pl.arek.motoappserver.domain.entities.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private Gson gson;

    /**
     *
     * Overrides authentication filter in order to recive login data as JSON
     *
     * @param request
     * @param response
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        User user = new User();
        try {
            user = gson.fromJson(request.getReader(), User.class);
        } catch (IOException ex) {
            Logger.getLogger(CustomUsernamePasswordAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());

        this.setDetails(request, authToken);

        return this.getAuthenticationManager().authenticate(authToken);

    }

}
