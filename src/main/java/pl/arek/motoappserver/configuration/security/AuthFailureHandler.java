/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.security;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;

/**
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    Gson gson;

    @Autowired
    SystemResponseFactory responseFactory;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.resetBuffer();

        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache");

        SystemResponse systemResponse = response(ResponseCode.WRONG_USERNAME_OR_PASSWORD);

        try {
            response.getOutputStream().print(gson.toJson(systemResponse));
        } catch (IOException ex) {
            Logger.getLogger(MySuccessLogoutHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private SystemResponse response(ResponseCode code) {
        return this.responseFactory.createSystemResponse(code);
    }
}
