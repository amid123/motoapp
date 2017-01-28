/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.account.impl;

import pl.arek.motoappserver.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.arek.motoappserver.services.account.AccountActivationMessageCreator;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Component
public class AccountActivationMessageCreatorImpl implements AccountActivationMessageCreator {

    @Autowired
    private Environment environment;

    public String getSubject() {
        return environment.getProperty("account.create.email.subject");
    }

    public String getSender() {
        return environment.getProperty("account.activator.email.sender");
    }

    public String buildActivationMessage(User user) {
        String message = environment.getProperty("account.create.email.message.template")
                + " "
                + environment.getProperty("application.host")
                + (environment.getProperty("application.port") == "80" ? "" : ":" + environment.getProperty("application.port"))
                + "/"
                + environment.getProperty("application.applicationName")
                + "/"
                + "account/activation/"
                + user.getActivationHash();
        return message;
    }
}
