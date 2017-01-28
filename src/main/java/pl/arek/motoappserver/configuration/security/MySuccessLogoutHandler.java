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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class MySuccessLogoutHandler implements LogoutSuccessHandler {

    @Autowired
    SystemResponseFactory responseFactory;

    @Autowired
    Gson gson;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication a) {

        response.resetBuffer();

        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache");

        SystemResponse systemResponse = response(ResponseCode.SUCCESS);

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
