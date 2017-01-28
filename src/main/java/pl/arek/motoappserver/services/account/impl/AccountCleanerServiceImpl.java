/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.account.impl;

import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.domain.repositories.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import pl.arek.motoappserver.services.account.AccountCleanerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Scope("singleton")
@Service
public class AccountCleanerServiceImpl implements AccountCleanerService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24) // 1000ms * 60s * 60m * 24h = 24h
    public void clean() {
        for (User u : userRepository.findUsersOlderThenAndActivated(7, false)) {
            userRepository.deleteUser(u);
        }
    }
}
