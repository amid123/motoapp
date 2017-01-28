/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.systemresponse.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class SystemResponseFactoryImpl implements SystemResponseFactory {

    @Autowired
    Environment environment;

    @Override
    public SystemResponse createSystemResponse(ResponseCode responseCode) {
        SystemResponse systemResponse = new SystemResponse();
        String message = getMessage(responseCode);
        systemResponse.setMessage(message);
        return systemResponse;
    }

    private String getMessage(ResponseCode responseCode) {
        String message = this.environment.getProperty(responseCode.getMessage());
        return message;
    }
}
