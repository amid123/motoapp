/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import pl.arek.motoappserver.configuration.security.LogedInChecker;
import pl.arek.motoappserver.services.account.AccountActivatorService;
import pl.arek.motoappserver.services.account.AccountCleanerService;
import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.services.account.impl.AccountActivatorServiceImpl;
import pl.arek.motoappserver.services.account.impl.AccountCleanerServiceImpl;
import pl.arek.motoappserver.services.account.impl.AccountServiceImpl;
import pl.arek.motoappserver.services.email.EmailSenderService;
import pl.arek.motoappserver.services.email.impl.EmailSenderServiceImpl;
import pl.arek.motoappserver.services.positioning.GlobalListManagerService;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;
import pl.arek.motoappserver.services.positioning.PersonalizedListBuilder;
import pl.arek.motoappserver.services.positioning.PersonalizedMemberList;
import pl.arek.motoappserver.services.positioning.impl.GlobalListManagerServiceImpl;
import pl.arek.motoappserver.services.positioning.impl.PersonalizedListManagerServiceImpl;
import pl.arek.motoappserver.services.profile.UserProfileService;
import pl.arek.motoappserver.services.profile.impl.UserProfileServiceImpl;
import pl.arek.motoappserver.services.positioning.PersonalizedListManagerService;
import pl.arek.motoappserver.services.positioning.impl.PersonalizedListBuilderImpl;
import pl.arek.motoappserver.services.settings.PersonalSettingsService;
import pl.arek.motoappserver.services.settings.impl.PersonalSettingsServiceImpl;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@Profile("main")
public class ServicesConfiguration {

    @Autowired
    private LogedInChecker loggedInChecker;

    /**
     *
     * Services here
     */
    @Bean
    @Scope("prototype")
    public AccountService accountService() {
        return new AccountServiceImpl(loggedInChecker);
    }

    @Bean
    @Scope("prototype")
    public AccountActivatorService accountActivatorService() {
        return new AccountActivatorServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public EmailSenderService emailSenderService() {
        return new EmailSenderServiceImpl();
    }

    @Bean
    @Scope("singleton")
    public AccountCleanerService accountCleanerService() {
        return new AccountCleanerServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public UserProfileService profileService() {
        return new UserProfileServiceImpl();
    }

    /**
     *
     *
     * here we have config for positioning functionality
     *
     * @return
     */
    @Bean
    @Scope("prototype")
    public GlobalListManagerService globalListManagerService() {
        return new GlobalListManagerServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public PersonalizedMemberList personalizedMemberList() {
        return new PersonalizedMemberList();
    }

    @Bean
    @Scope("prototype")
    public PersonalizedListManagerService membersPositionsListService() {
        return new PersonalizedListManagerServiceImpl();
    }

    /**
     * this must be singleton because its global list!!!
     *
     * @return
     */
    @Bean
    @Scope("singleton")
    public GlobalMemberList globalMemberList() {
        return new GlobalMemberList();
    }

    @Bean
    @Scope("prototype")
    PersonalSettingsService personalSettingsService() {
        return new PersonalSettingsServiceImpl();
    }

    @Bean
    @Scope("prototype")
    PersonalizedListBuilder personalizedListBuilder() {
        return new PersonalizedListBuilderImpl();
    }

}
