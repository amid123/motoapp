/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.email.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import pl.arek.motoappserver.services.email.EmailSenderService;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */

public class EmailSenderServiceImpl implements EmailSenderService {
    
    @Autowired
    MailSender mailSender;

 
    
    @Override
    public void sendEmail(String from, String to, String topic, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(topic);
        mailMessage.setText(message);
       
       
        this.mailSender.send(mailMessage);
        
    }
}
