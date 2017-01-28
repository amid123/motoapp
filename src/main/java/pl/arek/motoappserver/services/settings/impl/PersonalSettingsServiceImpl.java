/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.settings.impl;

import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.domain.entities.PersonalSettings;
import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.domain.repositories.PersonalSettingsRepository;
import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.services.settings.PersonalSettingsService;

/**
 *
 * @author agnieszka
 */
public class PersonalSettingsServiceImpl implements PersonalSettingsService{

    @Autowired
    AccountService accountService;

    @Autowired
    PersonalSettingsRepository settingsRepository;

    public double getMaxMembersCount() {
        User loggeUser = accountService.getCurrentUser();

        PersonalSettings settings = settingsRepository.getByUserName(loggeUser.getLogin());

        return settings.getMembersCount();
    }
}
