/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.security;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;


/**
 * 
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    Gson gson;

    @Autowired
    SystemResponseFactory responseFactory;
    
    

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.resetBuffer();

        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache");

        SystemResponse systemResponse = response(ResponseCode.ACCESS_DENIED);

        try {
            response.getOutputStream().print(gson.toJson(systemResponse));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private SystemResponse response(ResponseCode code) {
        return this.responseFactory.createSystemResponse(code);
    }
}
