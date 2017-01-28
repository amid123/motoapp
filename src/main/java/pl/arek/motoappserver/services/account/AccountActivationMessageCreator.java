/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.account;

import pl.arek.motoappserver.domain.entities.User;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface AccountActivationMessageCreator {

    public String getSubject();

    public String getSender();

    public String buildActivationMessage(User user);
}
