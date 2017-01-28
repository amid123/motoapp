/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.account.impl;

import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.domain.repositories.UserRepository;
import pl.arek.motoappserver.services.account.AccountActivatorService;
import pl.arek.motoappserver.services.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;
import pl.arek.motoappserver.services.account.AccountActivationMessageCreator;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Service
public class AccountActivatorServiceImpl implements AccountActivatorService {

    @Autowired
    EmailSenderService emailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountActivationMessageCreator activationMessage;

    @Autowired
    SystemResponseFactory responseFactory;

    @Override
    public SystemResponse activateAccount(String uuid) {

        User u = userRepository.findByUuid(uuid);
        // if u = null then there is no account with given uuid
        // we dont need to check it by user repo it could make unnecessary query to database;
        if (u != null) {
            if (u.getActivated()) {
                return this.response(ResponseCode.ACCOUNT_ALREADY_ACTIVATED);

            } else {
                u.setActivated(true);
                userRepository.updateUser(u);
                return this.response(ResponseCode.SUCCESS);
            }
        } else {
            return response(ResponseCode.ACCOUNT_NOT_EXIST);
        }

    }

    @Override
    public void sendActiovationEmail(User user) {
        String message = this.activationMessage.buildActivationMessage(user);
        String sender = this.activationMessage.getSender();
        String subject = this.activationMessage.getSubject();
        String email = user.getEmail();
        emailSender.sendEmail(sender, email, subject, message);
    }

    private SystemResponse response(ResponseCode code) {
        return this.responseFactory.createSystemResponse(code);
    }

}
